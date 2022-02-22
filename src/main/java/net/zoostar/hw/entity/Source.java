package net.zoostar.hw.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Source implements Persistable<String> {

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String id;

	private String sourceCode;
	
	private String name;
	
	private String baseUrl;
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public boolean isNew() {
		return !StringUtils.hasText(id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Source)) {
			return false;
		}
		Source other = (Source) obj;
		return Objects.equals(sourceCode, other.sourceCode);
	}

}
