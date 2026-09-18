package com.jknpp.gerenciamentopedidos.service;

import com.jknpp.gerenciamentopedidos.domain.Cliente;
import com.jknpp.gerenciamentopedidos.dto.ClienteRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ClienteResponseDTO;
import com.jknpp.gerenciamentopedidos.exception.BusinessException;
import com.jknpp.gerenciamentopedidos.exception.ResourceNotFoundException;
import com.jknpp.gerenciamentopedidos.mapper.ClienteMapper;
import com.jknpp.gerenciamentopedidos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDTO criarCliente(ClienteRequestDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("O e-mail já está em uso.");
        }
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("O CPF já está em uso.");
        }
            Cliente cliente = clienteMapper.toEntity(dto);

            Cliente clienteSalvo = clienteRepository.save(cliente);

            return clienteMapper.toResponseDTO(clienteSalvo);
        }
    public List<ClienteResponseDTO> listarClientes(){
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    public ClienteResponseDTO listarPorId(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO dto){
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
        if (!clienteExistente.getEmail().equals(dto.getEmail())
                && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("O e-mail já está em uso por outro cliente.");
        }
        if (!clienteExistente.getCpf().equals(dto.getCpf())
                && clienteRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("O CPF já está em uso por outro cliente.");
        }

        clienteMapper.updateEntityFromDto(dto, clienteExistente);

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        return clienteMapper.toResponseDTO(clienteAtualizado);
    }

    public void deletarPorId(Long id){
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
