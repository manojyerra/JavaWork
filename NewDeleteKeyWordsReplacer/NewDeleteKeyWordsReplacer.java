import java.util.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

class NewDeleteKeyWordsReplacer
{
    public static void main(String args[]) throws Exception
    {
         new NewDeleteKeyWordsReplacer(args[0]);
    }
    
    NewDeleteKeyWordsReplacer(String folderPath) throws Exception
    {
        ReplaceNewAndDeleteTraceInFolder(new File(folderPath));
    }
    
    void ReplaceNewAndDeleteTraceInFolder(File dir) throws Exception
    {
        File[] listFiles = dir.listFiles();
        
        for(int i=0; i<listFiles.length; i++)
        {
            File file = listFiles[i];
            
            if(file.isDirectory())
            {
                ReplaceNewAndDeleteTraceInFolder( file );
            }
            else
            {
                ReplaceNewAndDeleteTraceInFile(file);
            }
        }
    }
    
    void ReplaceNewAndDeleteTraceInFile(File file) throws Exception
    {
        Vector<String> lines = new Vector<String>();
        
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        String line = "";
        
        while( (line = br.readLine()) != null)
        {
			//if(!line.trim().startsWith("//"))
			//{			
			//	line = ReplaceNewTraceInLine(line);
			//	line = ReplaceDeleteTraceInLine(line);
			//	line = line.replaceAll("malloc", "mallocTrace");
			//	line = line.replaceAll("free", "freeTrace");
            //}
			
            lines.add(line);
        }
        
        br.close();
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        
        for(int i=0; i<lines.size(); i++)
        {
            String str = lines.get(i);
			
            bw.write(str, 0, str.length());
			bw.newLine();
        }
        
        bw.flush();
        bw.close();
    }
    
    String ReplaceNewTraceInLine(String line) throws Exception
    {
        int newOprIndex = line.indexOf("new ");
        
        if(newOprIndex == -1)
            return line;

        //Step1 : find preFix and remaining strings.
        String preFix = line.substring(0, newOprIndex);
        String remaining = line.substring(newOprIndex, line.length());
        
        
        //Step2 : find start and end brace index and postFix string.
        int startBraceIndex = getStartIndex(remaining, '(', '[');
        
        if(startBraceIndex == -1)
        {
            //System.out.println("invalid line<"+line+">");
            return line;
        }
        
        char startBrace = remaining.charAt(startBraceIndex);
        char endBrace = (startBrace == '(') ? ')' : ']';
        
        int endBraceIndex = getEndIndex(remaining, startBrace, endBrace);
        
        if(endBraceIndex == -1)
        {
            System.out.println("End brace not found <"+line+">");
            return line;
        }
        
        String postFix = remaining.substring(endBraceIndex+1, remaining.length());
        
        //Step3 : find the middleString and parse it.
        String middleStr = remaining.substring(0, endBraceIndex+1);
        String parseStrAfterNew = middleStr.substring(4, middleStr.length());
        String dataType = middleStr.substring(4, startBraceIndex);
        String middleStrAfterParse = "("+dataType+"*)newTrace ("+parseStrAfterNew+")";
        
        return preFix + middleStrAfterParse + postFix;
    }
    
    String ReplaceDeleteTraceInLine(String line) throws Exception
    {
        int deleteOprIndex = line.indexOf("delete ");
        
        if(deleteOprIndex == -1)
            return line;
        
        //Step1 : find preFix and remaining strings.
        String preFix = line.substring(0, deleteOprIndex);
        String remaining = line.substring(deleteOprIndex, line.length());
        
        
        //Step2 : find start and end brace index and postFix string.
        int colonIndex = remaining.indexOf(';');
        
        if(colonIndex == -1)
            return line;
        
        String postFix = remaining.substring(colonIndex, remaining.length());
        
        //Step3 : find the middleString and parse it.
        String middleStr = remaining.substring(0, colonIndex);
        String parseStrAfterNew = middleStr.substring(6, middleStr.length());
        String middleStrAfterParse = "deleteTrace("+parseStrAfterNew+")";
        
        return preFix + middleStrAfterParse + postFix;
    }
    
    public int getStartIndex(String ip, char ch1, char ch2) throws Exception
    {
        int startIndex1= ip.indexOf(ch1);
        int startIndex2= ip.indexOf(ch2);
        
        if(startIndex1 != -1 && startIndex2 != -1)
        {
            if(startIndex1 < startIndex2)
                return startIndex1;
            else
                return startIndex2;
        }
        else if(startIndex1 != -1)
        {
            return startIndex1;
        }
        else if(startIndex2 != -1)
        {
            return startIndex2;
        }
        else
        {
            return -1;
        }
    }
    
    public int getEndIndex(String ip, char startChar, char endChar) throws Exception
    {
        int count=0;
        int index=-1;
        
        for(int i=0; i < ip.length(); i++)
        {
            if(ip.charAt(i) == startChar)
            {
                count++;
            }
            else if(ip.charAt(i) == endChar)
            {
                count--;
                
                if(count == 0)
                    index=i;
            }
        }
        return index;
    }
}
