package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.Basket;
import ru.sstu.hobbyboard.entities.Purchase;
import ru.sstu.hobbyboard.entities.PurchaseComposition;
import ru.sstu.hobbyboard.repositories.PurchaseCompositionRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class PurchaseCompositionService {

    private final PurchaseCompositionRepository purchaseCompositionRepository;

    public PurchaseCompositionService(PurchaseCompositionRepository purchaseCompositionRepository) {
        this.purchaseCompositionRepository = purchaseCompositionRepository;
    }

    public Float getPrice(Set<PurchaseComposition> compositions) {
        return (float) compositions.stream().mapToDouble(x -> x.getCount() * (x.getPrice() -((x.getPrice()/100) * x.getDiscount()))).sum();
    }

    public Set<PurchaseComposition> getCompositionFromBasket(Set<Basket> baskets) {
        Set<PurchaseComposition> result = new HashSet<>();
        for (Basket basket : baskets) {
            PurchaseComposition composition = new PurchaseComposition();
            composition.setCount(basket.getCount());
            composition.setDiscount(basket.getProduct().getDiscount());
            composition.setPrice(basket.getProduct().getPrice());
            composition.setProduct(basket.getProduct());
            result.add(composition);
        }
        return result;
    }

//    public Set<PurchaseComposition> composition(Set<Basket> baskets) {
//        Set<PurchaseComposition> composition = new HashSet<>();
//        for (Basket b: baskets){
//            Purchase purchase = new Purchase();
//            purchase.
//            composition.add
//        }
//
//
//    }
}
