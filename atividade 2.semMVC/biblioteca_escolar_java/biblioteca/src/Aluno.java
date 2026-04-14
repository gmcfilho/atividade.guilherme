public class Aluno {

    private int id;
    private String nome;
    private String turma;
    private String matricula;

    public Aluno(int id, String nome, String turma, String matricula) {
        this.id = id;
        this.nome = nome;
        this.turma = turma;
        this.matricula = matricula;
    }

    public int getId()          { return id; }
    public String getNome()     { return nome; }
    public String getTurma()    { return turma; }
    public String getMatricula(){ return matricula; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Turma: %s | Matrícula: %s",
                id, nome, turma, matricula);
    }
}
