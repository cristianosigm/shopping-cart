package cs.home.shopping.service;

import cs.home.shopping.model.entity.Product;
import cs.home.shopping.model.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Transactional
    public List<Product> addAll(List<Product> items) {
        return this.productRepository.saveAll(items);
    }
}
