package net.zoostar.hw.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Product implements Persistable<String> {

	@Id
	private String id;
	
	private String sourceId;
	
	private String source;
	
	private String sku;
	
	private String type;
	
	private String description;
	
	private int itemCount;

	@Override
	public int hashCode() {
		return Objects.hash(sku, source);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		return Objects.equals(sku, other.sku) && Objects.equals(source, other.source);
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

}
