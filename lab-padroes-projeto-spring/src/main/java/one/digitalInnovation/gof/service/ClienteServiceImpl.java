package one.digitalInnovation.gof.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalInnovation.gof.model.Cliente;
import one.digitalInnovation.gof.model.ClienteRepository;
import one.digitalInnovation.gof.model.Endereco;
import one.digitalInnovation.gof.model.EnderecoRepository;

@Service
public class ClienteServiceImpl implements ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private ViaCepService viaCepService;
	
	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}
	
	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	private void salvarClienteComCep(Cliente cliente) {
		Endereco endereco = enderecoRepository.findById(cliente.getEndereco().getCep()).orElseGet(() -> {
	        String cep = cliente.getEndereco().getCep();
	        Endereco novoEndereco = viaCepService.consultarCep(cep);
	        enderecoRepository.save(novoEndereco);
	        return novoEndereco;
	    });

	    cliente.setEndereco(endereco);  
	    clienteRepository.save(cliente);
	}
	
	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.deleteById(id);
	}
	

}
