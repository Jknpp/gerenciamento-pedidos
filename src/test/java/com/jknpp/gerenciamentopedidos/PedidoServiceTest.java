package com.jknpp.gerenciamentopedidos;

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
import com.jknpp.gerenciamentopedidos.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve criar um pedido com sucesso quando os dados forem válidos")
    void deveCriarPedidoComSucesso() {

        Long clienteId = 1L;
        Long produtoId = 10L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setAtivo(true);

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setPreco(BigDecimal.valueOf(100.00));
        produto.setEstoque(10);
        produto.setAtivo(true);

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO();
        itemDto.setProdutoId(produtoId);
        itemDto.setQuantidade(2);

        PedidoRequestDTO requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(clienteId);
        requestDTO.setItens(List.of(itemDto));

        when(clienteRepository.findByIdAndAtivoTrue(clienteId)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findByIdAndAtivoTrue(produtoId)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(new PedidoResponseDTO());

        PedidoResponseDTO response = pedidoService.criarPedido(requestDTO);

        assertNotNull(response);
        verify(clienteRepository, times(1)).findByIdAndAtivoTrue(clienteId);
        verify(produtoRepository, times(1)).save(produto); // Verifica se atualizou o estoque
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando o estoque for insuficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        Long clienteId = 1L;
        Long produtoId = 10L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setAtivo(true);

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setPreco(BigDecimal.valueOf(100.00));
        produto.setEstoque(1);
        produto.setAtivo(true);

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO();
        itemDto.setProdutoId(produtoId);
        itemDto.setQuantidade(5);

        PedidoRequestDTO requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(clienteId);
        requestDTO.setItens(List.of(itemDto));

        when(clienteRepository.findByIdAndAtivoTrue(clienteId)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findByIdAndAtivoTrue(produtoId)).thenReturn(Optional.of(produto));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            pedidoService.criarPedido(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o cliente for inexistente")
    void deveLancarExcecaoQuandoClienteInexistente(){
        Long clienteId = 1L;

        PedidoRequestDTO requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(clienteId);

        when(clienteRepository.findByIdAndAtivoTrue(clienteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.criarPedido(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Cliente"));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o produto for inexistente")
    void deveLancarExcecaoProdutoInexistente(){
        Long clienteId = 1L;
        Long produtoInexistenteId = 99L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setAtivo(true);

        ItemPedidoRequestDTO itemDto = new ItemPedidoRequestDTO();
        itemDto.setProdutoId(produtoInexistenteId);
        itemDto.setQuantidade(1);

        PedidoRequestDTO requestDTO = new PedidoRequestDTO();
        requestDTO.setClienteId(clienteId);
        requestDTO.setItens(List.of(itemDto));

        when(clienteRepository.findByIdAndAtivoTrue(clienteId)).thenReturn(Optional.of(cliente));

        when(produtoRepository.findByIdAndAtivoTrue(produtoInexistenteId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.criarPedido(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Produto"));
    }

    @Test
    @DisplayName("Deve atualizar o status para CANCELADO com sucesso e devolver itens ao estoque")
    void deveCancelarPedidoComSucessoEDevolverEstoque() {
        Long pedidoId = 1L;
        Long produtoId = 10L;

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setEstoque(5);

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setItens(List.of(item));

        StatusPedidoRequestDTO dto = new StatusPedidoRequestDTO();
        dto.setStatusPedido(StatusPedido.CANCELADO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(new PedidoResponseDTO());

        PedidoResponseDTO response = pedidoService.atualizarStatusPedido(pedidoId, dto);

        assertNotNull(response);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        assertEquals(7, produto.getEstoque());
        verify(produtoRepository, times(1)).save(produto);
        verify(pedidoRepository, times(1)).save(pedido);
    }
}

