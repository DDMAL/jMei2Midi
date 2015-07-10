/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ddmal.jmei2midi;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author dinamix
 */
public class MusicFileFilter implements FileFilter 
{
    private final String[] goodFileExtensions = new String[]{"mei"};
    
    @Override
    public boolean accept(File pathname) 
    {
        for (String extension : goodFileExtensions) 
        {
            if(pathname.getName().toLowerCase().endsWith(extension)) 
            {
                return true;
            }
        }
        return false;
    }
    
}
