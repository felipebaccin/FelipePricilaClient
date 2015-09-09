package br.univel.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.univel.model.Disciplina;

public class ModeloDisciplina extends AbstractTableModel {

	List<Disciplina> disciplina = new ArrayList<Disciplina>();

	public ModeloDisciplina(List<Disciplina> disciplina) {
		this.disciplina = disciplina;
	}

	
	public int getColumnCount() {
		return 2;
	}

	
	public int getRowCount() {
		return disciplina.size();
	}

	
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return disciplina.get(rowIndex).getId();
		case 1:
			return disciplina.get(rowIndex).getNome();
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
