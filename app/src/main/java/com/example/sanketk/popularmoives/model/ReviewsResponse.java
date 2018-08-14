package com.example.sanketk.popularmoives.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse{
	@SerializedName("id")
	private int id;
	@SerializedName("page")
	private int page;
	@SerializedName("total_pages")
	private int totalPages;
	@SerializedName("results")
	private List<ReviewsResultsItem> reviewsResults;
	@SerializedName("total_results")
	private int totalResults;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setReviewsResults(List<ReviewsResultsItem> reviewsResults){
		this.reviewsResults = reviewsResults;
	}

	public List<ReviewsResultsItem> getReviewsResults(){
		return reviewsResults;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"ReviewsResponse{" + 
			"id = '" + id + '\'' + 
			",page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",reviews_results = '" + reviewsResults + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}