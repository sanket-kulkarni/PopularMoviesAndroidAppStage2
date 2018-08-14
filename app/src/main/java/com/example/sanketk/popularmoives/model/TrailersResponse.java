package com.example.sanketk.popularmoives.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse{
	@SerializedName("results")
	private List<TrailerResultsItem> trailerResults;
	@SerializedName("id")
	private int id;

	public void setTrailerResults(List<TrailerResultsItem> trailerResults){
		this.trailerResults = trailerResults;
	}

	public List<TrailerResultsItem> getTrailerResults(){
		return trailerResults;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"TrailersResponse{" + 
			"trailer_results = '" + trailerResults + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}