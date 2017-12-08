package dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class FiltrosDAO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;


	public List certificadosAlunosImprimir(Long idAluno, int sit) {
		Query query = null;
	
			query = manager.createQuery(
					"from Certificado a where a.status is true and a.alunoTurma = " + idAluno + " and a.situacao = " + sit);
			return query.getResultList();

	}
	
	public List certificadosAlunos(Long idAluno, int sit) {
		Query query = null;
	
			query = manager.createQuery(
					"from Certificado a where a.status is true and a.aluno = " + idAluno + " and a.situacao = " + sit);
			return query.getResultList();
	
	}

	public List certificadosAlunosComAtividade(Long idAluno, int sit, Long idAtividadeTurma) {
		Query query = null;
	
			query = manager.createQuery("from Certificado a where a.status is true and a.alunoTurma = " + idAluno
					+ " and a.situacao = " + sit + " and atividadeTurma = " + idAtividadeTurma);
	
		return query.getResultList();
	}
	

	public List atividadesTurmaAluno(Long idTurma) {
		Query query = null;
	
			query = manager.createQuery("from AtividadeTurma a where a.status is true and a.matriz = " + idTurma);
	
		return query.getResultList();
	}

	public List gruposTurmaAluno(Long idTurma) {
		Query query = null;
	
			query = manager.createQuery("from GrupoTurma a where a.status is true and a.turma = " + idTurma);
	
		return query.getResultList();
	}
	


	public List buscarTurmaAluno(Long idAluno) {
		Query query = null;
	
			query = manager.createQuery("from AlunoTurma a where "
					+ " a.dataMudanca = (select max(b.dataMudanca) from AlunoTurma b where b.aluno = " + idAluno + ") "
					+ " and a.aluno = " + idAluno + "");
	
		return query.getResultList();
	}

	public List buscarAlunoTurma(Long idTurma) {
		Query query = null;
	
			query = manager.createQuery("from AlunoTurma a where "
					+ " a.dataMudanca = (select max(b.dataMudanca) from AlunoTurma b where b.turma = " + idTurma + " and "
							+ " a.aluno = b.aluno )");
	
		return query.getResultList();
	}
}
