package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Address;
import one.digitalinnovation.gof.model.AddressRepository;
import one.digitalinnovation.gof.model.Client;
import one.digitalinnovation.gof.model.ClientRepository;
import one.digitalinnovation.gof.service.ClientService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public  Iterable<Client> buscarTodos(){
        return clientRepository.findAll();
    }

    @Override
    public Client buscarPorId(Long id){
        Optional<Client> client = clientRepository.findById(id);
        return client.get();
    }

    @Override
    public void inserir(Client client){
        completarCep(client);
    }



    @Override
    public void atualizar(Long id, Client client){
        Optional<Client> clientBd = clientRepository.findById(id);
        if(clientBd.isPresent()){
            completarCep(client);
        }
    }

    @Override
    public void deletar(Long id){
        clientRepository.deleteById(id);
    }

    private void completarCep(Client client) {
        String cep = client.getAdress().getCep();
        Address address = addressRepository.findById(cep).orElseGet(() ->{
            Address newAddress = viaCepService.consultarCep(cep);
            addressRepository.save(newAddress);
            return newAddress;
        });
        client.setAdress(address);
        clientRepository.save(client);
    }
}
