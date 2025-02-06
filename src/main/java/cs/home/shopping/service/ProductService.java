package cs.home.shopping.service;

import cs.home.shopping.dto.ProductDTO;
import cs.home.shopping.exception.ItemNotFoundException;
import cs.home.shopping.model.entity.Product;
import cs.home.shopping.model.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public List<ProductDTO> findAll() {
        return mapToDTO(productRepository.findAll());
    }

    public List<ProductDTO> addAll(List<ProductDTO> items) {
        return mapToDTO(productRepository.saveAll(mapToEntity(items)));
    }

    public ProductDTO findById(Long id) {
        return mapper.map(productRepository.findById(id)
            .orElseThrow(ItemNotFoundException::new), ProductDTO.class);
    }

    private List<ProductDTO> mapToDTO(List<Product> items) {
        return items.stream()
            .map(it -> mapper.map(it, ProductDTO.class))
            .toList();
    }

    private List<Product> mapToEntity(List<ProductDTO> items) {
        return items.stream()
            .map(it -> mapper.map(it, Product.class))
            .toList();
    }
}

