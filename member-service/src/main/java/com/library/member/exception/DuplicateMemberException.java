package com.library.member.exception;

public class DuplicateMemberException extends RuntimeException{
	
	public DuplicateMemberException(String msg)
	{
		super(msg);
	}
}
