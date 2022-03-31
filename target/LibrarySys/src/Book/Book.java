package dev.khe.DiagramRecover;

import java.io.Serializable;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StreamProvider;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public abstract class Book implements Serializable{
	private int id;
	private String name;
	private String author;
	private boolean rental;
	public boolean equlas(int id) {
		if (this.id == id)
			return true;
		else
			return false;
	}

	public Book() {

		id = 0;
		name = "���� ������ ������ �ߴ�.";
		author = "������";
		rental = false;
	}

	public Book(int id, String name, String author) {
		this.id = id;
		this.name = name;
		this.author = author;
		rental = false;
	}

	public abstract int getLateFees(int days);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isRental() {
		return rental;
	}

	public void setRental(boolean rental) {
		this.rental = rental;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", rental=" + rental + "]";
	}

}
