package minor.refactoring.instancegen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

public abstract class FlatPublisherReader
{
	private int currentLineNumber;

	protected void generateException (String message) throws FlatPublisherReaderException
	{
		throw new FlatPublisherReaderException(message + " in line " + currentLineNumber);
	}
	
	protected abstract void registerEntity(String type, String name, Hashtable<String, String> attributes) throws FlatPublisherReaderException;
	
	private void readEntity(StringTokenizer tokenizer) throws FlatPublisherReaderException
	{
		Hashtable<String, String> attributes = new Hashtable<String, String>();
		
		if (!tokenizer.hasMoreElements())
			generateException("missing entity type");
		
		String entityType = tokenizer.nextToken().trim();
		
		if (!tokenizer.hasMoreElements())
			generateException("missing entity name");

		String entityName = tokenizer.nextToken().trim();
		
		while (tokenizer.hasMoreElements())
		{
			String attributeName = tokenizer.nextToken().trim();
			
			if (!tokenizer.hasMoreElements())
				generateException("missing relation attribute value");

			String attributeValue = tokenizer.nextToken().trim();
			attributes.put(attributeName, attributeValue);
		}
		
		registerEntity(entityType, entityName, attributes);
	}

	protected abstract void registerRelationship(String type, String source, String target, Hashtable<String, String> attributes) throws FlatPublisherReaderException;
	
	private void readRelationship(StringTokenizer tokenizer) throws FlatPublisherReaderException
	{
		Hashtable<String, String> attributes = new Hashtable<String, String>();
		
		if (!tokenizer.hasMoreElements())
			generateException("missing relationship type");
		
		String relationshipType = tokenizer.nextToken().trim();
		
		if (!tokenizer.hasMoreElements())
			generateException("missing relationship source");

		String relationshipSource = tokenizer.nextToken().trim();
		
		if (!tokenizer.hasMoreElements())
			generateException("missing relationship target");

		String relationshipTarget = tokenizer.nextToken().trim();
		
		while (tokenizer.hasMoreElements())
		{
			String attributeName = tokenizer.nextToken().trim();
			
			if (!tokenizer.hasMoreElements())
				generateException("missing relationship attribute value");

			String attributeValue = tokenizer.nextToken().trim();
			attributes.put(attributeName, attributeValue);
		}
		
		registerRelationship(relationshipType, relationshipSource, relationshipTarget, attributes);
	}
	
	private void processLine(String line) throws FlatPublisherReaderException
	{
		StringTokenizer tokenizer = new StringTokenizer(line, ";");

		if (tokenizer.countTokens() == 0)
			return;
		
		String lineType = tokenizer.nextToken().trim();
		
		if (lineType.compareToIgnoreCase("E") == 0)
			readEntity(tokenizer);
		
		else if (lineType.compareToIgnoreCase("R") == 0)
			readRelationship(tokenizer);
		
		else
			generateException("Invalid token in line start (found: " + lineType + "; expected 'E' or 'R'");
	}

	public void cleanUp() throws FlatPublisherReaderException
	{		
	}
	
	public void execute(String filename) throws FlatPublisherReaderException
	{
		currentLineNumber = 0;
		Scanner scanner;
		
		try
		{
			scanner = new Scanner(new FileInputStream(filename));
		}
		catch(IOException e)
		{
			throw new FlatPublisherReaderException(e.getMessage());
		}
		
		try
		{
			while (scanner.hasNextLine())
			{
				currentLineNumber++;
				processLine(scanner.nextLine());
			}

			cleanUp();
		}
		finally
		{
			scanner.close();
		}
	}
}