package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer   idEmprestimo;
    private Integer   idAluno;
    private Integer   idLivro;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrazoDevolucao;
    private LocalDate dataDevolucao;

    public Emprestimo() { }

    public Emprestimo(Integer idEmprestimo,
                      Integer idAluno,
                      Integer idLivro,
                      LocalDate dataEmprestimo,
                      LocalDate dataPrazoDevolucao,
                      LocalDate dataDevolucao) {

        this.idEmprestimo = idEmprestimo;
        this.idAluno = Objects.requireNonNull(idAluno);
        this.idLivro = Objects.requireNonNull(idLivro);
        this.dataEmprestimo = Objects.requireNonNull(dataEmprestimo);
        this.dataPrazoDevolucao = Objects.requireNonNull(dataPrazoDevolucao);
        this.dataDevolucao = dataDevolucao;
    }

    public Integer getIdEmprestimo() {
        return idEmprestimo;
    }

    public void setIdEmprestimo(Integer idEmprestimo) {
        this.idEmprestimo = idEmprestimo;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = Objects.requireNonNull(idAluno);
    }

    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = Objects.requireNonNull(idLivro);
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = Objects.requireNonNull(dataEmprestimo);
    }

    public LocalDate getDataPrazoDevolucao() {
        return dataPrazoDevolucao;
    }

    public void setDataPrazoDevolucao(LocalDate dataPrazoDevolucao) {
        this.dataPrazoDevolucao = Objects.requireNonNull(dataPrazoDevolucao);
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isDevolvido() {
        return dataDevolucao != null;
    }

    public long diasDeAtraso(LocalDate dataReferencia) {
        if (dataDevolucao != null && dataReferencia.isAfter(dataDevolucao)) {
            return ChronoUnit.DAYS.between(dataPrazoDevolucao, dataDevolucao);
        } else if (dataDevolucao == null && dataReferencia.isAfter(dataPrazoDevolucao)) {
            return ChronoUnit.DAYS.between(dataPrazoDevolucao, dataReferencia);
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmprestimo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Emprestimo other)) return false;
        return Objects.equals(idEmprestimo, other.idEmprestimo);
    }

    @Override
    public String toString() {
        return "Emprestimo[id=%d, aluno=%d, livro=%d, emprestado=%s, prazo=%s, devolucao=%s]"
                .formatted(idEmprestimo, idAluno, idLivro,
                        dataEmprestimo, dataPrazoDevolucao, dataDevolucao);
    }
}
