package rift2Core.frontend

/*
* @Author: Ruige Lee
* @Date:   2021-03-19 16:24:13
* @Last Modified by:   Ruige Lee
* @Last Modified time: 2021-03-24 11:23:40
*/




/*
  Copyright (c) 2020 - 2021 Ruige Lee <wut.ruigeli@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/


import chisel3._
import chisel3.util._
import rift2Core.basicElement._


class Decode16 (x:UInt) {

	val info = Wire(new Info_instruction)


	def addi4spnImm = Cat(0.U(54.W), x(10,7), x(12,11), x(5), x(6), 0.U(2.W)).asSInt()
	def lwImm = Cat(0.U(57.W), x(5), x(12,10), x(6), 0.U(2.W)).asSInt()
	def ldImm = Cat(0.U(56.W), x(6,5), x(12,10), 0.U(3.W)).asSInt()
	def lwspImm = Cat(0.U(56.W), x(3,2), x(12), x(6,4), 0.U(2.W)).asSInt()
	def ldspImm = Cat(0.U(55.W), x(4,2), x(12), x(6,5), 0.U(3.W)).asSInt()
	def swspImm = Cat(0.U(56.W), x(8,7), x(12,9), 0.U(2.W)).asSInt()
	def sdspImm = Cat(0.U(55.W), x(9,7), x(12,10), 0.U(3.W)).asSInt()
	def luiImm = Cat(Fill(47, x(12)), x(6,2), 0.U(12.W)).asSInt()
	def addi16spImm = Cat(Fill(55, x(12)), x(4,3), x(5), x(2), x(6), 0.U(4.W)).asSInt()
	def addiImm = Cat(Fill(59, x(12)), x(6,2)).asSInt()
	def jImm = Cat(Fill(53, x(12)), x(8), x(10,9), x(6), x(7), x(2), x(11), x(5,3), 0.U(1.W)).asSInt()
	def bImm = Cat(Fill(56, x(12)), x(6,5), x(2), x(11,10), x(4,3), 0.U(1.W)).asSInt()
	def shamtImm = Cat(x(12), x(6,2))


	def c_addi4spn = ( x === BitPat("b????????????????000???????????00") ) & (x(12,5) =/= 0.U)
	def c_fld      = ( x === BitPat("b????????????????001???????????00") )
	def c_lw       = ( x === BitPat("b????????????????010???????????00") )
	def c_ld       = ( x === BitPat("b????????????????011???????????00") )
	def c_fsd      = ( x === BitPat("b????????????????101???????????00") )
	def c_sw       = ( x === BitPat("b????????????????110???????????00") )
	def c_sd       = ( x === BitPat("b????????????????111???????????00") )
	def c_nop      = ( x === BitPat("b????????????????0000000000000001") )
	def c_addi     = ( x === BitPat("b????????????????000???????????01") ) & (x(11,7) =/= 0.U)
	def c_addiw    = ( x === BitPat("b????????????????001???????????01") ) & (x(11,7) =/= 0.U)
	def c_li       = ( x === BitPat("b????????????????010???????????01") ) & (x(11,7) =/= 0.U)
	def c_addi16sp = ( x === BitPat("b????????????????011?00010?????01") ) & (x(11,7) === 2.U) & (Cat(x(12), x(6,2)) =/= 0.U)
	def c_lui      = ( x === BitPat("b????????????????011???????????01") ) & (x(11,7) =/= 2.U) & (x(12,2) =/= 0.U)
	def c_srli     = ( x === BitPat("b????????????????100?00????????01") ) & (Cat(x(12), x(6,2)) =/= 0.U)
	def c_srai     = ( x === BitPat("b????????????????100?01????????01") ) & (Cat(x(12), x(6,2)) =/= 0.U)

	def c_andi     = ( x === BitPat("b????????????????100?10????????01") )
	def c_sub      = ( x === BitPat("b????????????????100011???00???01") )
	def c_xor      = ( x === BitPat("b????????????????100011???01???01") )
	def c_or       = ( x === BitPat("b????????????????100011???10???01") )
	def c_and      = ( x === BitPat("b????????????????100011???11???01") )
	def c_subw     = ( x === BitPat("b????????????????100111???00???01") )
	def c_addw     = ( x === BitPat("b????????????????100111???01???01") )
	def c_j        = ( x === BitPat("b????????????????101???????????01") )
	def c_beqz     = ( x === BitPat("b????????????????110???????????01") )
	def c_bnez     = ( x === BitPat("b????????????????111???????????01") )
	def c_slli     = ( x === BitPat("b????????????????000???????????10") ) & (x(11,7) =/= 0.U)
	def c_fldsp    = ( x === BitPat("b????????????????001???????????10") )
	def c_lwsp     = ( x === BitPat("b????????????????010???????????10") ) & (x(11,7) =/= 0.U)
	def c_ldsp     = ( x === BitPat("b????????????????011???????????10") ) & (x(11,7) =/= 0.U)
	def c_jr       = ( x === BitPat("b????????????????1000?????0000010") ) & (x(11,7) =/= 0.U)
	def c_mv       = ( x === BitPat("b????????????????1000??????????10") ) & (x(11,2) =/= 0.U)
	def c_ebreak   = ( x === BitPat("b????????????????1001000000000010") )
	def c_jalr     = ( x === BitPat("b????????????????1001?????0000010") ) & (x(11,7) =/= 0.U)
	def c_add      = ( x === BitPat("b????????????????1001??????????10") ) & (x(11,2) =/= 0.U)
	def c_fsdsp    = ( x === BitPat("b????????????????101???????????10") )
	def c_swsp     = ( x === BitPat("b????????????????110???????????10") )
	def c_sdsp     = ( x === BitPat("b????????????????111???????????10") )


	info.param.rd0_raw        :=
		MuxCase( 0.U, Array(
				( x === BitPat("b????????????????0?????????????00") ) -> Cat(1.U(2.W), x(4,2)),
				( x === BitPat("b????????????????0?????????????01") ) -> x(11,7),
				( x === BitPat("b????????????????1?????????????01") ) -> Cat(1.U(2.W), x(9,7)),
				( x === BitPat("b??????????????????????????????10") ) -> x(11,7)
			)
		)


	info.param.rs1_raw        := 
		MuxCase( 0.U, Array(
				( x === BitPat("b??????????????????????????????00") ) -> Cat(1.U(2.W), x(9,7)),
				( x === BitPat("b????????????????0?????????????01") ) -> x(11,7),
				( x === BitPat("b????????????????1?????????????01") ) -> Cat(1.U(2.W), x(9,7)),
				( x === BitPat("b??????????????????????????????10") ) -> x(11,7)
			)
		)


	info.param.rs2_raw       := 
		MuxCase( 0.U, Array(
				( x === BitPat("b????????????????1?????????????00") ) -> Cat(1.U(2.W), x(4,2)),
				( x === BitPat("b????????????????0?????????????01") ) -> x(11,7),
				( x === BitPat("b????????????????100???????????01") ) -> Cat(1.U(2.W), x(4,2)),
				( x === BitPat("b????????????????100???????????10") ) -> x(6,2)
			)
		)
	
	info.param.rs3_raw       := 0.U


	info.param.imm        :=
		MuxCase( 0.S, Array(
				c_addi4spn -> addi4spnImm,
				c_fld -> ldImm,
				c_lw -> lwImm,
				c_ld -> ldImm,
				c_fsd -> ldImm,
				c_sw -> lwImm,
				c_sd -> ldImm,
				c_addi -> addiImm,
				c_addiw -> addiImm,
				c_li ->  addiImm,
				c_addi16sp -> addi16spImm,
				c_lui -> luiImm,
				c_andi ->addiImm,
				c_j -> jImm,
				c_beqz -> bImm,
				c_bnez -> bImm,
				c_fldsp -> ldspImm,
				c_lwsp -> lwspImm,
				c_ldsp -> ldspImm,
				c_fsdsp -> sdspImm,
				c_swsp -> swspImm,
				c_sdsp -> sdspImm,
				c_slli -> shamtImm,
				c_srli -> shamtImm,
				c_srai -> shamtImm,

			)
		)





	info.alu_isa.lui         := c_lui
	info.alu_isa.auipc       := false.B
	info.alu_isa.addi        := c_addi4spn | c_nop | c_addi | c_addi16sp
	info.alu_isa.addiw       := c_addiw
	info.alu_isa.slti        := false.B
	info.alu_isa.sltiu       := false.B
	info.alu_isa.xori        := false.B
	info.alu_isa.ori         := false.B
	info.alu_isa.andi        := c_andi
	info.alu_isa.slli        := c_slli
	info.alu_isa.slliw       := false.B
	info.alu_isa.srli        := c_srli
	info.alu_isa.srliw       := false.B
	info.alu_isa.srai        := c_srai
	info.alu_isa.sraiw       := false.B
	info.alu_isa.add         := c_mv | c_add
	info.alu_isa.addw        := c_addw
	info.alu_isa.sub         := c_sub
	info.alu_isa.subw        := c_subw
	info.alu_isa.sll         := false.B
	info.alu_isa.sllw        := false.B
	info.alu_isa.slt         := false.B
	info.alu_isa.sltu        := false.B
	info.alu_isa.xor         := c_xor
	info.alu_isa.srl         := false.B
	info.alu_isa.srlw        := false.B
	info.alu_isa.sra         := false.B
	info.alu_isa.sraw        := false.B
	info.alu_isa.or          := c_or
	info.alu_isa.and         := c_and

	info.bru_isa.jal         := c_j
	info.bru_isa.jalr        := c_jr | c_jalr
	info.bru_isa.beq         := c_beqz
	info.bru_isa.bne         := c_bnez
	info.bru_isa.blt         := false.B
	info.bru_isa.bge         := false.B
	info.bru_isa.bltu        := false.B
	info.bru_isa.bgeu        := false.B

	info.lsu_isa.lb          := false.B
	info.lsu_isa.lh          := false.B
	info.lsu_isa.lw          := c_lw | c_lwsp
	info.lsu_isa.ld          := c_ld | c_ldsp
	info.lsu_isa.lbu         := false.B
	info.lsu_isa.lhu         := false.B
	info.lsu_isa.lwu         := false.B
	info.lsu_isa.sb          := false.B
	info.lsu_isa.sh          := false.B
	info.lsu_isa.sw          := c_sw | c_swsp
	info.lsu_isa.sd          := c_sd | c_sdsp
	info.lsu_isa.fence       := false.B
	info.lsu_isa.fence_i     := false.B

	info.csr_isa.rw          := false.B
	info.csr_isa.rs          := false.B
	info.csr_isa.rc          := false.B
	info.csr_isa.rwi         := false.B
	info.csr_isa.rsi         := false.B
	info.csr_isa.rci         := false.B

	info.mul_isa.mul         := false.B
	info.mul_isa.mulh        := false.B
	info.mul_isa.mulhsu      := false.B
	info.mul_isa.mulhu       := false.B
	info.mul_isa.div         := false.B
	info.mul_isa.divu        := false.B
	info.mul_isa.rem         := false.B
	info.mul_isa.remu        := false.B
	info.mul_isa.mulw        := false.B
	info.mul_isa.divw        := false.B
	info.mul_isa.divuw       := false.B
	info.mul_isa.remw        := false.B
	info.mul_isa.remuw       := false.B

	info.privil_isa.ecall       := false.B
	info.privil_isa.ebreak      := c_ebreak
	info.privil_isa.mret        := false.B
	info.privil_isa.uret        := false.B
	info.privil_isa.sret        := false.B
	info.privil_isa.dret        := false.B

	info.privil_isa.wfi         := false.B

	info.privil_isa.sfence_vma  := false.B

	info.privil_isa.hfence_vvma := false.B
	info.privil_isa.hfence_gvma := false.B

	info.privil_isa.hlv_b       := false.B
	info.privil_isa.hlv_bu      := false.B
	info.privil_isa.hlv_h       := false.B
	info.privil_isa.hlv_hu      := false.B
	info.privil_isa.hlvx_hu     := false.B
	info.privil_isa.hlv_w       := false.B
	info.privil_isa.hlvx_wu     := false.B
	info.privil_isa.hsv_b       := false.B
	info.privil_isa.hsv_h       := false.B
	info.privil_isa.hsv_w       := false.B
	info.privil_isa.hlv_wu      := false.B
	info.privil_isa.hlv_d       := false.B
	info.privil_isa.hsv_d       := false.B

	info.lsu_isa.lr_w        := false.B
	info.lsu_isa.sc_w        := false.B
	info.lsu_isa.amoswap_w   := false.B
	info.lsu_isa.amoadd_w    := false.B
	info.lsu_isa.amoxor_w    := false.B
	info.lsu_isa.amoand_w    := false.B
	info.lsu_isa.amoor_w     := false.B
	info.lsu_isa.amomin_w    := false.B
	info.lsu_isa.amomax_w    := false.B
	info.lsu_isa.amominu_w   := false.B
	info.lsu_isa.amomaxu_w   := false.B
	info.lsu_isa.lr_d        := false.B
	info.lsu_isa.sc_d        := false.B
	info.lsu_isa.amoswap_d   := false.B
	info.lsu_isa.amoadd_d    := false.B
	info.lsu_isa.amoxor_d    := false.B
	info.lsu_isa.amoand_d    := false.B
	info.lsu_isa.amoor_d     := false.B
	info.lsu_isa.amomin_d    := false.B
	info.lsu_isa.amomax_d    := false.B
	info.lsu_isa.amominu_d   := false.B
	info.lsu_isa.amomaxu_d   := false.B

	info.lsu_isa.flw         := false.B
	info.lsu_isa.fsw         := false.B
	info.fpu_isa.fmadd_s     := false.B
	info.fpu_isa.fmsub_s     := false.B
	info.fpu_isa.fnmsub_s    := false.B
	info.fpu_isa.fnmadd_s    := false.B
	info.fpu_isa.fadd_s      := false.B
	info.fpu_isa.fsub_s      := false.B
	info.fpu_isa.fmul_s      := false.B
	info.fpu_isa.fdiv_s      := false.B
	info.fpu_isa.fsqrt_s     := false.B
	info.fpu_isa.fsgnj_s     := false.B
	info.fpu_isa.fsgnjn_s    := false.B
	info.fpu_isa.fsgnjx_s    := false.B
	info.fpu_isa.fmin_s      := false.B
	info.fpu_isa.fmax_s      := false.B
	info.fpu_isa.fcvt_w_s    := false.B
	info.fpu_isa.fcvt_wu_s   := false.B
	info.fpu_isa.fmv_x_w     := false.B
	info.fpu_isa.feq_s       := false.B
	info.fpu_isa.flt_s       := false.B
	info.fpu_isa.fle_s       := false.B
	info.fpu_isa.fclass_s    := false.B
	info.fpu_isa.fcvt_s_w    := false.B
	info.fpu_isa.fcvt_s_wu   := false.B
	info.fpu_isa.fmv_w_x     := false.B
	info.fpu_isa.fcvt_l_s    := false.B
	info.fpu_isa.fcvt_lu_s   := false.B
	info.fpu_isa.fcvt_s_l    := false.B
	info.fpu_isa.fcvt_s_lu   := false.B

	info.lsu_isa.fld         := c_fld | c_fldsp
	info.lsu_isa.fsd         := c_fsd | c_fsdsp
	info.fpu_isa.fmadd_d     := false.B
	info.fpu_isa.fmsub_d     := false.B
	info.fpu_isa.fnmsub_d    := false.B
	info.fpu_isa.fnmadd_d    := false.B
	info.fpu_isa.fadd_d      := false.B
	info.fpu_isa.fsub_d      := false.B
	info.fpu_isa.fmul_d      := false.B
	info.fpu_isa.fdiv_d      := false.B
	info.fpu_isa.fsqrt_d     := false.B
	info.fpu_isa.fsgnj_d     := false.B
	info.fpu_isa.fsgnjn_d    := false.B
	info.fpu_isa.fsgnjx_d    := false.B
	info.fpu_isa.fmin_d      := false.B
	info.fpu_isa.fmax_d      := false.B
	info.fpu_isa.fcvt_s_d    := false.B
	info.fpu_isa.fcvt_d_s    := false.B
	info.fpu_isa.feq_d       := false.B
	info.fpu_isa.flt_d       := false.B
	info.fpu_isa.fle_d       := false.B
	info.fpu_isa.fclass_d    := false.B
	info.fpu_isa.fcvt_w_d    := false.B
	info.fpu_isa.fcvt_wu_d   := false.B
	info.fpu_isa.fcvt_d_w    := false.B
	info.fpu_isa.fcvt_d_wu   := false.B
	info.fpu_isa.fcvt_l_d    := false.B
	info.fpu_isa.fcvt_lu_d   := false.B
	info.fpu_isa.fmv_x_d     := false.B
	info.fpu_isa.fcvt_d_l    := false.B
	info.fpu_isa.fcvt_d_lu   := false.B
	info.fpu_isa.fmv_d_x     := false.B

}

