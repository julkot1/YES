package pl.julkot1.yes.ast.models;

public interface IParental<T> {
    void addToParent(Argument argument);
    T get();
}
