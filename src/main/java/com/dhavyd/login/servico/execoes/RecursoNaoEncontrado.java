package com.dhavyd.login.servico.execoes;

public class RecursoNaoEncontrado extends RuntimeException{
    
    public RecursoNaoEncontrado(String msg) {
        super(msg);
    }
}
