package com.jknpp.gerenciamentopedidos.service;


import com.jknpp.gerenciamentopedidos.domain.Produto;
import com.jknpp.gerenciamentopedidos.dto.ProdutoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ProdutoResponseDTO;
import com.jknpp.gerenciamentopedidos.exception.BusinessException;
import com.jknpp.gerenciamentopedidos.exception.ResourceNotFoundException;
import com.jknpp.gerenciamentopedidos.mapper.ProdutoMapper;
import com.jknpp.gerenciamentopedidos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto){
        if(produtoRepository.existsByNome(dto.getNome())){
            throw new BusinessException("Já existe um produto cadastrado com o nome: " + dto.getNome());
        }

        Produto produto = produtoMapper.toEntity(dto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

    public List<ProdutoResponseDTO> listarProdutos(){
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(produtoMapper::toDTO)
                .toList();
    }

    public ProdutoResponseDTO listarPorId(Long id){
        Produto produto = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no id: " + id));

        return produtoMapper.toDTO(produto);
    }

    public ProdutoResponseDTO alterarProduto(Long id, ProdutoRequestDTO dto){
        Produto produtoExistente = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no id: " + id));

        if(!produtoExistente.getNome().equalsIgnoreCase(dto.getNome()) && produtoRepository.existsByNome(dto.getNome())){
            throw new BusinessException("Já existe um produto cadastrado com o nome: " + dto.getNome());
        }

        produtoMapper.updateEntityFromDto(dto, produtoExistente);
        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return produtoMapper.toDTO(produtoAtualizado);
    }

    public void deletarProduto(Long id){
        Produto produto = produtoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado no id: " + id));

        produto.setAtivo(false);
        produtoRepository.save(produto);
    }
}
