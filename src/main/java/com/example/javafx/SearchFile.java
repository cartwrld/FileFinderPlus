package com.example.javafx;

public class SearchFile
{
	private String[] search;
	private String fileType;
	private String fullPath;
	private String startPath;

	public SearchFile(String[] s, String ft, String p)
	{
		this.search = s;
		this.fileType = ft;
		this.fullPath = p;

	}

	public String[] getSearch()
	{
		return search;
	}
	public String getFileType()
	{
		return fileType;
	}
	public String getFullPath()
	{
		return fullPath;
	}

//	@Override
//	public String toString()
//	{
//		return String.format("%s | %s | %s", search.toString(), fileType, fullPath);
//	}

}
