
package com.mycompany.auctionserver;

/**
 *
 * @author Cristopher
 */
public class User {
    
    private String name, cpf, signature, text;

    public User(String name, String cpf, String signature, String text) {
        this.name = name;
        this.cpf = cpf;
        this.signature = signature;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", cpf=" + cpf + ", signature=" + signature + ", text=" + text + '}';
    }
    
    
}
