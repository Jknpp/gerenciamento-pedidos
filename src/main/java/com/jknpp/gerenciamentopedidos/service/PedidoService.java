package com.jknpp.gerenciamentopedidos.service;

import com.jknpp.gerenciamentopedidos.domain.*;
import com.jknpp.gerenciamentopedidos.dto.ItemPedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.PedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.PedidoResponseDTO;
import com.jknpp.gerenciamentopedidos.dto.StatusPedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.exception.BusinessException;
import com.jknpp.gerenciamentopedidos.exception.ResourceNotFoundException;
import com.jknpp.gerenciamentopedidos.mapper.PedidoMapper;
import com.jknpp.gerenciamentopedidos.repository.ClienteRepository;
import com.jknpp.gerenciamentopedidos.repository.PedidoRepository;
import com.jknpp.gerenciamentopedidos.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado ou inativo com o id: " + dto.getClienteId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now());

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findByIdAndAtivoTrue(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ou inativo com o id: " + itemDto.getProdutoId()));

            if(itemDto.getQuantidade() <= 0){
                throw new BusinessException("Quantidade precisa ser maior que zero");
            }
            if (produto.getEstoque() < itemDto.getQuantidade()) {
                throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome() +
                        ". Disponível: " + produto.getEstoque());
            }

            produto.setEstoque(produto.getEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDto.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());

            itens.add(itemPedido);

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade()));
            itemPedido.setSubtotal(subtotal);
            valorTotal = valorTotal.add(subtotal);

        }
        pedido.setItens(itens);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return pedidoMapper.toDTO(pedidoSalvo);
    }

    public List<PedidoResponseDTO> listarPedidos(){
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toDTO)
                .toList();
    }

    public PedidoResponseDTO listarPedidosId(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));
        return pedidoMapper.toDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatusPedido(Long id, StatusPedidoRequestDTO dto){
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));

        StatusPedido statusAtual = pedidoExistente.getStatus();
        StatusPedido novoStatus = dto.getStatusPedido();

        if (statusAtual == StatusPedido.CANCELADO) {
            throw new BusinessException("Um pedido cancelado não pode ter seu status alterado.");
        }

        if (statusAtual == StatusPedido.PAGO && novoStatus == StatusPedido.PENDENTE) {
            throw new BusinessException("Um pedido pago não pode voltar para pendente.");
        }

        if (novoStatus == StatusPedido.CANCELADO && statusAtual != StatusPedido.CANCELADO) {
            for (ItemPedido item : pedidoExistente.getItens()) {
                Produto produto = item.getProduto();
                produto.setEstoque(produto.getEstoque() + item.getQuantidade());
                produtoRepository.save(produto);
            }
        }
        pedidoExistente.setStatus(novoStatus);
        Pedido pedidoAtualizado = pedidoRepository.save(pedidoExistente);

        return pedidoMapper.toDTO(pedidoAtualizado);
    }
}
