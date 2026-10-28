package com.jknpp.gerenciamentopedidos.controller;

import com.jknpp.gerenciamentopedidos.dto.PedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.PedidoResponseDTO;
import com.jknpp.gerenciamentopedidos.dto.StatusPedidoRequestDTO;
import com.jknpp.gerenciamentopedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(dto));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(){
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> listarPedidoId(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.listarPedidosId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,@RequestBody StatusPedidoRequestDTO dto){
        PedidoResponseDTO status = pedidoService.atualizarStatusPedido(id, dto);
        return ResponseEntity.ok(status);
    }
}
