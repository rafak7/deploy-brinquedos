package com.fiap.brinquedos.entity;
import lombok.* ;
import jakarta.persistence.*;

@Entity
@SequenceGenerator(name="invs", sequenceName = "SQ_TB_BRINQUEDO", allocationSize = 1)
@Table (name = "TB_BRINQUEDO")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Brinquedo {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY, generator = "invs")
    @Column(name = "BRINQUEDO_ID")
    private Long idBrinquedo;

    @Column(name = "BRINQUEDO_NM", length = 500, nullable = false )
    private String brinquedoNm;

    @Column(name = "BRINQUEDO_TP", length = 500, nullable = false)
    private String brinquedoTp;

    @Column(name = "BRINQUEDO_CLASSICACAO", length = 500, nullable = false)
    private String brinquedoClassificacao;

    @Column(name = "BRINQUEDO_TAM", length = 500, nullable = false)
    private String brinquedoTam;

    @Column(name = "BRINQUEDO_PRECO", length = 10, nullable = false)
    private Integer brinquedoPreco;


}
