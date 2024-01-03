/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaproject.ViewModels;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import valonnue.javaproject.Models.Movie;

/**
 *
 * @author daniel.bele
 */
public class MovieTableModel extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"Id", "Title",  "Description", "Banner path", "Link", "Published date", "Showing Date"};
    
    private List<Movie> movies;

    public MovieTableModel(List<Movie> movies) {
        this.movies = movies;
    }

    public void setArticles(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        //return Article.class.getDeclaredFields().length - 1;
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getId();
            case 1:
                return movies.get(rowIndex).getTitle();
            case 2:
                return movies.get(rowIndex).getDescription();
            case 3:
                return movies.get(rowIndex).getBannerPath();
            case 4:
                return movies.get(rowIndex).getLink();
            case 5:
                return movies.get(rowIndex).getPublishedDate();
            case 6:
                return movies.get(rowIndex).getShowingDate();
            default:
                throw new RuntimeException("No such column");
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }


    // important for the id ordering
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
        }
        return super.getColumnClass(columnIndex); 
    }
 
    
}