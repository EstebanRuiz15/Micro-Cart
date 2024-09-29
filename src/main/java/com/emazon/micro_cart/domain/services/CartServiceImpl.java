package com.emazon.micro_cart.domain.services;

import com.emazon.micro_cart.domain.exception.ErrorExceptionCategoriesRepit;
import com.emazon.micro_cart.domain.exception.ErrorExceptionConflict;
import com.emazon.micro_cart.domain.exception.ErrorExceptionQuantity;
import com.emazon.micro_cart.domain.exception.ErrorNotFoudArticle;
import com.emazon.micro_cart.domain.exception.ErrorNotFoundArticleToDelete;
import com.emazon.micro_cart.domain.interfaces.ICartServicePort;
import com.emazon.micro_cart.domain.interfaces.ICartPersistance;
import com.emazon.micro_cart.domain.interfaces.IRepositoryItemsPort;
import com.emazon.micro_cart.domain.interfaces.IStockServicePort;
import com.emazon.micro_cart.domain.model.ArticlesMod;
import com.emazon.micro_cart.domain.model.Cart;
import com.emazon.micro_cart.domain.model.CartItems;
import com.emazon.micro_cart.domain.model.PaginItems;
import com.emazon.micro_cart.domain.util.ConstantsDomain;

import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartServiceImpl implements ICartServicePort {
    private final IStockServicePort stockService;
    private final ICartPersistance CartPersistance;
    private final IRepositoryItemsPort repositoryItems;
    Cart cart;

    public CartServiceImpl(IStockServicePort stockService, ICartPersistance repository,
            IRepositoryItemsPort repositoryItems) {
        this.stockService = stockService;
        this.CartPersistance = repository;
        this.repositoryItems = repositoryItems;
    }

    @Override
    public String addItemsToCart(Integer id, Integer quantity) {
        Integer userid = CartPersistance.getClientId();
        Optional<CartItems> Itemcart = repositoryItems.findByProductIdAndUserId(id, userid);
        Integer itemQuantity = stockService.validItemQuantity(id);
        Integer numInCart = ConstantsDomain.ZERO;

        if (!stockService.validItemExist(id)) {
            throw new ErrorNotFoudArticle(ConstantsDomain.NOT_FOUND_ARTICLE + id);
        }

        if (Itemcart.isPresent()) {
            numInCart = Itemcart.get().getQuantity();
        }

        if (itemQuantity == ConstantsDomain.ZERO) {
            throw new ErrorExceptionQuantity(ConstantsDomain.MESSAGE_DATE_ARRIVE_SUPPLY);
        }

        if (itemQuantity <= quantity + numInCart) {
            throw new ErrorExceptionQuantity(
                    ConstantsDomain.MESSAGE_ONLY_HAVE + itemQuantity + ConstantsDomain.PRODUCTS);
        }

        if (!CartPersistance.findByUserId(userid).isPresent()) {
            cart = createNewCart(userid);
            CartPersistance.save(cart);
            addProductToCart(cart, id, quantity);
            return ConstantsDomain.ADD_WITH_EXIT;
        }

        if (CartPersistance.findByUserId(userid).isPresent()) {
            cart = CartPersistance.findByUserId(userid).get();
            addItemIfCarExist(cart, id, quantity, userid);

            return ConstantsDomain.ADD_WITH_EXIT;
        }

        return ConstantsDomain.ADD_WITH_EXIT;
    };

    @Override
    public String deleteItemsToCart(Integer idArticle, Integer quantity) {
        Integer clientId = CartPersistance.getClientId();
        Optional<CartItems> items = repositoryItems.findByProductIdAndUserId(idArticle, clientId);
        if (!items.isPresent()) {
            throw new ErrorNotFoundArticleToDelete(ConstantsDomain.NOT_ARTICLE_TO_DELETE);
        }
        CartItems item = items.get();
        if ((item.getQuantity() - quantity) <= ConstantsDomain.ZERO) {
            repositoryItems.delete(item);
        }
        if ((item.getQuantity() - quantity) > ConstantsDomain.ZERO) {
            item.setQuantity(item.getQuantity() - quantity);
            repositoryItems.save(item);
        }
        Cart cart = CartPersistance.findByUserId(clientId).get();
        cart.setModiDate(LocalDateTime.now());
        CartPersistance.save(cart);
        return ConstantsDomain.DELETE_WHIT_SUCESS;
    }

    @Override
    public PaginItems getAllIemsToCart(Integer size, Integer page, String OrderBy, String filterBy, String nameFilter) {

        Integer userid = CartPersistance.getClientId();
        cart = CartPersistance.findByUserId(userid).get();
        if (cart == null) {
            throw new ErrorExceptionConflict(ConstantsDomain.NO_FOUND_CART);
        }
        List<Integer> ids = repositoryItems.getAllArticlesId(cart.getId());
        if (ids == null) {
            throw new ErrorNotFoudArticle(ConstantsDomain.NO_ARTICLES_IN_CART);
        }

        List<ArticlesMod> itemsInCart = stockService.allArticles(ids);
        itemsInCart = setQuantityInCart(itemsInCart, cart.getId());
        String totalpay = getTotalToPay(itemsInCart);

        if (filterBy != null && nameFilter != null && filterBy.equalsIgnoreCase(ConstantsDomain.CATEGORIES)) {
            itemsInCart = filterByCategory(itemsInCart, nameFilter);
        }

        if (filterBy != null && nameFilter != null && filterBy.equalsIgnoreCase(ConstantsDomain.BRAND)) {
            itemsInCart = filterByBrand(itemsInCart, nameFilter);
        }

        itemsInCart = OrderByName(itemsInCart, OrderBy);
        itemsInCart = existQuantityInStockMessage(itemsInCart);

        return getItemsPaginated(itemsInCart, size, page, totalpay);
    }

    @Override
    public List<CartItems> getAllItems (){
        Integer idUser=CartPersistance.getClientId();
        Optional<Cart> cart=CartPersistance.findByUserId(idUser);
        if(!cart.isPresent()){
            throw new ErrorNotFoudArticle(ConstantsDomain.NOTHING_THAT_BUY);
        }
        if(repositoryItems.getAllArticlesId(cart.get().getId())==null){
            throw new ErrorNotFoudArticle(ConstantsDomain.NOTHING_THAT_BUY);
        }
        
      return repositoryItems.getAllItems(cart.get().getId());
    }

    @Override
    public boolean updateCart(){
        Integer userId=CartPersistance.getClientId();
        Cart cart=CartPersistance.findByUserId(userId).orElse(null);
        repositoryItems.deleteByCartId(cart.getId());
        return true;
    }

    private Cart createNewCart(Integer userId) {
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setCreationDate(LocalDateTime.now());

        return newCart;
    }

    private void addProductToCart(Cart cart, Integer productId, Integer quantity) {
        CartItems cartItem = new CartItems();
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        CartPersistance.addItemToCart(cart, cartItem);
    }

    private void addItemIfCarExist(Cart cart, Integer id, Integer quantity, Integer userid) {

        List<Integer> ids = repositoryItems.getAllArticlesId(cart.getId());
        if (!stockService.validCategories(ids)) {
            throw new ErrorExceptionCategoriesRepit(ConstantsDomain.ERROR_3_CATEGORIES_REPEAT);
        }
        Optional<CartItems> cartItem = repositoryItems.findByProductIdAndUserId(id, userid);
        if (cartItem.isPresent()) {
            CartItems item = cartItem.get();
            cart.setModiDate(LocalDateTime.now());
            item.setQuantity(item.getQuantity() + quantity);
            CartPersistance.save(cart);
            repositoryItems.save(item);
        }
        if (!cartItem.isPresent()) {
            cart.setModiDate(LocalDateTime.now());
            addProductToCart(cart, id, quantity);
        }

    }

    private List<ArticlesMod> setQuantityInCart(List<ArticlesMod> listArticles, Integer idCart) {
        List<CartItems> items = repositoryItems.getAllItems(idCart);
        listArticles.forEach(item -> {
            items.stream()
                    .filter(pc -> pc.getProductId().equals(item.getId()))
                    .findFirst()
                    .ifPresent(pc -> {
                        item.setQuantityInCart(pc.getQuantity());
                    });
        });
        return listArticles;
    }

    private PaginItems getItemsPaginated(List<ArticlesMod> items, Integer size, Integer page, String totalpay) {

        Integer totalItems = items.size();
        Integer from = Math.min(page * size, totalItems);
        Integer to = Math.min((page + ConstantsDomain.ONE) * size, totalItems);

        List<ArticlesMod> articlepage = items.subList(from, to);

        Integer totalPage = (int) Math.ceil((double) totalItems / size);

        if (articlepage.isEmpty()) {
            throw new ErrorExceptionConflict(ConstantsDomain.NO_ARTICLES_FOUND_EXCEPTION_MESSAGE +
                    ConstantsDomain.TOTAL_PAGES + totalPage);
        }
        return new PaginItems(
                articlepage,
                page,
                size,
                totalPage,
                totalItems,
                totalpay);
    }

    private List<ArticlesMod> OrderByName(List<ArticlesMod> lisArticles, String Order) {
        Comparator<ArticlesMod> comparator;

        comparator = Comparator.comparing(ArticlesMod::getName, String.CASE_INSENSITIVE_ORDER);
        if (ConstantsDomain.DESC.equalsIgnoreCase(Order)) {
            comparator = comparator.reversed();
        }

        return lisArticles.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<ArticlesMod> existQuantityInStockMessage(List<ArticlesMod> articles) {
        for (ArticlesMod article : articles) {
            if (article.getQuantityStock() == ConstantsDomain.ZERO) {
                article.setStockMessage(ConstantsDomain.MESSAGE_DATE_ARRIVE_SUPPLY);
            } else {
                article.setStockMessage(null);
            }
        }
        return articles;
    }

    private List<ArticlesMod> filterByBrand(List<ArticlesMod> articles, String brandName) {
        return articles.stream()
                .filter(article -> article.getBrandName().equalsIgnoreCase(brandName))
                .collect(Collectors.toList());
    }

    private List<ArticlesMod> filterByCategory(List<ArticlesMod> articles, String categoryName) {
        return articles.stream()
                .filter(article -> article.getCategories()
                        .stream()
                        .anyMatch(category -> category.getName().equalsIgnoreCase(categoryName)))
                .collect(Collectors.toList());
    }

    private String getTotalToPay(List<ArticlesMod> articles) {
        double totalPrice = articles.stream()
                .mapToDouble(article -> article.getPrice() * article.getQuantityInCart())
                .sum();

        return String.format("%.2f", totalPrice);
    }


}
