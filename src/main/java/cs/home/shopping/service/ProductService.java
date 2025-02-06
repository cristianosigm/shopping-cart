package cs.home.shopping.service;

import cs.home.shopping.dto.ProductDTO;
import cs.home.shopping.model.mapper.ProductMapper;
import cs.home.shopping.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<ProductDTO> findAll() {
        return this.mapper.mapToDTO(this.productRepository.findAll());
    }

    public List<ProductDTO> addAll(List<ProductDTO> items) {
        return this.mapper.mapToDTO(this.productRepository.saveAll(this.mapper.mapToEntity(items)));
    }
}

