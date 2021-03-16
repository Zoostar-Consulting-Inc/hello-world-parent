package net.zoostar.hw.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
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
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String id;
	
	@Column(nullable = false, length = 20)
	private String sku;

	@Column(nullable = false, length = 20)
	private String assetId;
	
	@Column(nullable = false, length = 20)
	private String source;
	
	@Column(nullable = false, length = 20)
	private String sourceId;
	
	@Column(nullable = false, length = 20)
	private String type;
	
	@Column(nullable = true, length = 100)
	private String description;
	
	private int itemCount;

	@Override
	public int hashCode() {
		return Objects.hash(assetId, sku, source);
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
		return Objects.equals(assetId, other.assetId) && Objects.equals(sku, other.sku)
				&& Objects.equals(source, other.source);
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

}
