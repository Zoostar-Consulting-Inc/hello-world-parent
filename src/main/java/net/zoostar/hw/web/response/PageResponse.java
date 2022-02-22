package net.zoostar.hw.web.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageResponse<T> {

	public PageResponse(Page<T> page) {
		this.pageNum = page.getNumber();
		this.size = page.getSize();
		this.contents = page.getContent();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.empty = page.isEmpty();
		this.numberOfElements = page.getNumberOfElements();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}
	
	private int pageNum;
	
	private int size;
	
	private List<T> contents;
	
	private boolean first;
	
	private boolean last;
	
	private boolean empty;
	
	private int numberOfElements;
	
	private long totalElements;
	
	private int totalPages;
	
}
