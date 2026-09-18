package com.jknpp.gerenciamentopedidos.controller;

import com.jknpp.gerenciamentopedidos.dto.ClienteRequestDTO;
import com.jknpp.gerenciamentopedidos.dto.ClienteResponseDTO;
import com.jknpp.gerenciamentopedidos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criarCliente(@RequestBody @Valid ClienteRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criarCliente(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes(){
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> listarClienteId(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.listarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id,@RequestBody @Valid ClienteRequestDTO dto){
        return ResponseEntity.ok(clienteService.atualizarCliente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id){
        clienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
