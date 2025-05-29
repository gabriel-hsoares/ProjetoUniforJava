package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Aluno implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idAluno;
    private String  nomeAluno;
    private String  matricula;
    private LocalDate dataNascimento;

    public Aluno() { }

    public Aluno(Integer idAluno,
                 String nomeAluno,
                 String matricula,
                 LocalDate dataNascimento) {

        this.idAluno = idAluno;
        setNomeAluno(nomeAluno);
        setMatricula(matricula);
        this.dataNascimento = dataNascimento;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = Objects.requireNonNull(nomeAluno, "Nome não pode ser nulo");
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        if (matricula == null || matricula.length() != 7) {
            throw new IllegalArgumentException("Matrícula deve ter exatamente 7 caracteres");
        }
        this.matricula = matricula;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAluno);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Aluno other)) return false;
        return Objects.equals(idAluno, other.idAluno);
    }

    @Override
    public String toString() {
        return "Aluno[id=%d, nome='%s', matrícula='%s', nascimento=%s]"
                .formatted(idAluno, nomeAluno, matricula, dataNascimento);
    }
}
