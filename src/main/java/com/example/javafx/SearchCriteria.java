package com.example.javafx;

public class SearchCriteria
{
	private String[] search;
	private String fileType;
	private String path;
	private String folder;


	public SearchCriteria(String s, String ft, String p)
	{
		this.search = s.split(",");
		this.fileType = ft;
		this.path = p;

	}

	public String[] getSearch()
	{
		return search;
	}
	public String getFileType()
	{
		return fileType;
	}
	public String getPath()
	{
		return path;
	}

	public void setFolderName(String fn)
	{
		this.folder = "~" + fn + "~";
	}
	public String getFolderName()
	{
		return this.folder == null ? "No folder set" : this.folder;
	}


	public String getSearchStr()
	{
		String str = "";

		for (String s : search)
		{
			str += s;
		}

		return str;
	}

	//	@Override
//	public String toString()
//	{
//		return String.format("%s | %s | %s", search.toString(), fileType, fullPath);
//	}

}
