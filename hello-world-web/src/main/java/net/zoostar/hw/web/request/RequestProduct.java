package net.zoostar.hw.web.request;

import lombok.ToString;
import net.zoostar.hw.model.Product;

@ToString
public class RequestProduct implements RequestEntity<Product> {
	
	private Product product;

	public RequestProduct(String sku) {
		product = new Product(sku);
	}
	
	public String getSku() {
		return product.getSku();
	}
	
	public void setName(String name) {
		product.setName(name);
	}
	
	public String getName() {
		return product.getName();
	}
	
	public void setDesc(String desc) {
		product.setDesc(desc);
	}
	
	public String getDesc() {
		return product.getDesc();
	}
	
	@Override
	public Product toEntity() {
		return product;
	}

}
