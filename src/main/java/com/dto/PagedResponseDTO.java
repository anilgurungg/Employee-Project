package com.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class PagedResponseDTO<T> {
	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean last;
	
	public PagedResponseDTO() {

	}

	public PagedResponseDTO(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
		setContent(content);
		this.page = page;
		this.size = size;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.last = last;
	}

	public List<T> getContent() {
		return content == null ? null : new ArrayList<>(content);
	}

	public final void setContent(List<T> content) {
		if (content == null) {
			this.content = null;
		} else {
			this.content = Collections.unmodifiableList(content);
		}
	}



	public boolean isLast() {
		return last;
	}
	
}
