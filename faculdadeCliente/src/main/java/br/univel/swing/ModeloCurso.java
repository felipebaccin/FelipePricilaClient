package br.univel.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.univel.model.Curso;

public class ModeloCurso extends AbstractTableModel {

	List<Curso> cursos = new ArrayList<Curso>();

	public ModeloCurso(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public int getColumnCount() {
		return 2;
	}

	
	public int getRowCount() {
		return cursos.size();
	}

	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return cursos.get(rowIndex).getId();
		case 1:
			return cursos.get(rowIndex).getNome();
		default:
			break;
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "ID";
		case 1:
			return "NOME";
		default:
			break;
		}
		return super.getColumnName(column);
	}

}
