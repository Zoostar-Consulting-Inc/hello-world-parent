package net.zoostar.hw.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Product;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductRequest implements EntityMapper<Product> {

	private String source;
	
	private String sourceId;

	private String sku;
	
	private String desc;

	public ProductRequest(Product entity) {
		this.desc = entity.getName();
		this.sku = entity.getSku();
		this.source = entity.getSource();
		this.sourceId = entity.getSourceId();
	}
	
	@Override
	public Product toEntity() {
		var entity = new Product();
		entity.setName(desc);
		entity.setSku(sku);
		entity.setSource(source);
		entity.setSourceId(sourceId);
		return entity;
	}

}
