package ru.sstu.hobbyboard.servicies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.entities.Purchase;
import ru.sstu.hobbyboard.repositories.PurchaseRepository;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public boolean isUserPurchase(Integer id, Purchase purchase) {
        return purchaseRepository.findPurchasesByUserId(id).stream().anyMatch(x -> x.getId().equals(purchase.getId()));
    }

    public void delete(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }

    public void deleteById(Integer id) {
        purchaseRepository.deleteById(id);
    }

    public Purchase findById(Integer id) {
        return purchaseRepository.findById(id).orElse(null);
    }

    public Page<Purchase> findAllPageable(PageRequest pageable) {
        return purchaseRepository.findAll(pageable);
    }

    public Integer countByUserId(Integer id) {
        return purchaseRepository.countByUserId(id);
    }
}
