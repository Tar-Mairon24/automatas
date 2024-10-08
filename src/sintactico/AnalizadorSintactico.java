package sintactico;

import java.util.ArrayList;

public class AnalizadorSintactico {
	private int puntero;
	private final ArrayList<Token> tokens;
	private int token;
	private boolean error = false;

	public AnalizadorSintactico(ArrayList<Token> tokens) {
		this.tokens = tokens;
		puntero = 0;
		token = tokens.get(puntero).getValorTablaTokens();
	}

	public boolean analizar() {
		return programa();
	}

	public void avanzar() {
		if (puntero < tokens.size() - 1) {
			puntero++;
			token = tokens.get(puntero).getValorTablaTokens();
		}
		if (puntero == tokens.size() - 1 && token != -3) {
			token = -99999;
			System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra 'end'");
			error = true;
		}
	}

	private boolean programa() {
		if (token == -1) {
			avanzar();
			if (token == -55) {
				avanzar();
				if (token == -75) {
					avanzar();
					if (token == -15) {
						avanzar();
						if (declarativa()) {
							if (codigo()) {
								return true;
							}
						}
					} else
						return true;
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
						error = true;
					}
					return false;
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra reservada 'var'");
					error = true;
				}
				return false;
			}
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba la palabra reservada 'program'");
				error = true;
			}
			return false;
		}
		return (tokens.get(puntero).getValorTablaTokens() != -3 || tokens.get(puntero).getValorTablaTokens() != -75) && puntero == 2;
	}

	private boolean declarativa() {
		if (tipo()) {
			avanzar();
			if (token == -77) {
				avanzar();
				if (identificadores()) {
					if (token == -75) {
						avanzar();
						if (token == -11 || token == -12 || token == -13 || token == -14)
							return declarativa();
						else
							return true;
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
							error = true;
						}
						return false;
					}
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un identificador valido");
						error = true;
					}
					return false;
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ':'");
					error = true;
				}
				return false;
			}
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un tipo valido");
				error = true;
			}
			return false;
		}
	}

	private boolean codigo() {
		if (token == -2) {
			avanzar();
			if (estructuras()) {
				if (token == -3) {
					return true;
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea()
								+ ": Se esperaba la palabra reservada 'end'");
						error = true;
					}
					return false;
				}
			}
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea()
						+ ": Se esperaba la palabra reservada 'begin'");
				error = true;
			}
			return false;
		}
		return false;
	}

	private boolean estructuras() {
		if (token == -4) {
			if (read()) {
				return estructuras();
			}
		} else if (token == -5) {
			if (write()) {
				return estructuras();
			}
		} else if (token == -6) {
			if (if_else()) {
				return estructuras();
			}
		} else if (token == -8) {
			if (while_()) {
				return estructuras();
			}
		} else if (token == -9) {
			if (repeat_until()) {
				return estructuras();
			}
		} else if (token == -3) {
			return true;
		} else {
			if (asignacion()) {
				return estructuras();
			}
		}
		return true;
	}

	private boolean read() {
		if (token == -4) {
			avanzar();
			if (token == -73) {
				avanzar();
				if (identificador()) {
					avanzar();
					if (token == -74) {
						avanzar();
						if (token == -75) {
							avanzar();
							return true;
						} else {
							if(!error){
								System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
										+ "Se esperaba el caractér ';' ");
								error = true;
							}
							return false;
						}
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
									+ "Se esperaba el caractér ')' ");
							error = true;
						}
						return false;
					}
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
								+ "Se esperaba un identificador valido");
						error = true;
					}
					return false;
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
							+ "Se esperaba el caractér '(' ");
					error = true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean write() {
		if (token == -5) {
			avanzar();
			if (token == -73) {
				avanzar();
				if (identificador() || constante()) {
					avanzar();
					if (token == -74) {
						avanzar();
						if (token == -75) {
							avanzar();
							return true;
						} else {
							if(!error){
								System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
										+ "Se esperaba el caractér ';' ");
								error = true;
							}
							return false;
						}
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
									+ "Se esperaba el caractér ')' ");
							error = true;
						}
						return false;
					}
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
								+ "Se esperaba un identificador valido");
						error = true;
					}
					return false;
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
							+ "Se esperaba el caractér '(' ");
					error = true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean if_else() {
		if (token == -6) {
			avanzar();
			if (token == -73) {
				avanzar();
				if (expresion()) {
					if (token == -74) {
						avanzar();
						if (token == -16) {
							avanzar();
							if (codigo()) {
								avanzar();
								if (token == -7) {
									avanzar();
									if (codigo()) {
										if(token == -3 && puntero < tokens.size() - 1){
											avanzar();
											return true;
										}
										else{
											if(!error){
												System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
														"Se esperaba la palabra 'end' ");
												error = true;
											}
											return false;
										}
									} else
										return false;
								} else
									return true;
							}
						} else {
							if(!error){
								System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
										+ "Se esperaba la palabra 'then' ");
								error = true;
							}
							return false;
						}
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
									+ "Se esperaba el caractér ')' ");
							error = true;
						}
						return false;
					}
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": "
							+ "Se esperaba el caractér '(' ");
					error = true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean while_() {
		if (token == -8) {
			avanzar();
			if (token == -73) {
				avanzar();
				if (expresion()) {
					if (token == -74) {
						avanzar();
						if (token == -17) {
							avanzar();
							if (codigo()) {
								if(token == -3 && puntero < tokens.size() - 1){
									avanzar();
									return true;
								}
								else{
									if(!error){
										System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
												"Se esperaba la palabra 'end' ");
										error = true;
									}
									return false;
								}
							}
						} else {
							if(!error){
								System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
										"Se esperaba la palabra 'do' ");
								error = true;
							}
							return false;
						}
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
									"Se esperaba el caractér ')' ");
							error = true;
						}
						return false;
					}
				}
			}else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
							"Se esperaba el caractér '(' ");
					error = true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean repeat_until() {
		if (token == -9) {
			avanzar();
			if (codigo()) {
				avanzar();
				if (token == -10) {
					avanzar();
					if (token == -73) {
						avanzar();
						if (expresion()) {
							if (token == -74) {
								avanzar();
								if (token == -75) {
									avanzar();
									return true;
								} else {
									if(!error){
										System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
												"Se esperaba un ';'");
										error = true;
									}
									return false;
								}
							} else {
								if(!error) {
									System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
											"Se esperaba el caractér ')' ");
									error = true;
								}
								return false;
							}
						}
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
									"Se esperaba el caractér '(' ");
							error = true;
						}
						return false;
					}
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
								"Se esperaba la palabra 'until' ");
						error = true;
					}
					return false;
				}
			}
		}
		return false;
	}

	private boolean tipo() {
		if (token == -11 || token == -12 || token == -13 || token == -14)
			return true;
		else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
						tokens.get(puntero).getLexema() + " no es un tipo valido");
				error = true;
			}
			return false;
		}
	}

	private boolean identificador() {
		return token == -51 || token == -52 || token == -53 || token == -54;
//        else {
//                System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
//                        tokens.get(puntero).getLexema() + " no es un identificador valido");
//            return false;
//        }
	}

	private boolean identificadores() {
		if (identificador()) {
			avanzar();
			if (token == -76) {
				avanzar();
				return identificadores();
			} else return true;
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un identificador valido");
				error = true;
			}
			return false;
		}
	}

	private boolean operador_logico() {
		return (token == -31 || token == -32 || token == -33 || token == -34 || token == -35 || token == -36 || token == -41 || token == -42 || token == -43);
	}

	private boolean operador_aritmetico() {
		return (token == -21 || token == -22 || token == -24 || token == -25 || token == -27);
	}

	private boolean constante() {
		return (token == -61 || token == -62 || token == -63 || token == -64 || token == -65);
	}

	private boolean asignacion() {
		if (identificador()) {
			avanzar();
			if (token == -26) {
				avanzar();
				if (expresion()) {
					if (token == -75) {
						avanzar();
						return true;
					} else {
						if(!error){
							System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ';'");
							error = true;
						}
						return false;
					}
				}
			} else {
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ':='");
					error = true;
				}
				return false;
			}
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": " +
						tokens.get(puntero).getLexema() + " no es un identificador valido");
				error = true;
			}
			return false;
		}
		return false;
	}

	private boolean expresion() {
		if (termino()) {
			if (operador_logico()) {
				avanzar();
				return termino();
			} else {
				if (constante() || identificador() || token == -75 || token == -74)
					return true;
				if(!error){
					System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un operador logico");
					error = true;
				}
				return false;
			}
		}
		return false;
	}

	private boolean termino() {
		if (factor()) {
			if (operador_aritmetico()) {
				avanzar();
				return termino();
			} else {
				if (token == -73 || token == -43) {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un operador aritmetico");
						error = true;
					}
					return false;
				}
				if (constante() || identificador())
					return true;
				return true;
			}
		}
		return false;
	}

	private boolean factor() {
		if (constante() || identificador()) {
			avanzar();
			return true;
		} else if (token == -73) {
			avanzar();
			if (expresion()) {
				if (token == -74) {
					avanzar();
					return true;
				} else {
					if(!error){
						System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un ')'");
						error = true;
					}
					return false;
				}
			}
		} else if (token == -43) {
			avanzar();
			return factor();
		} else {
			if(!error){
				System.err.println("Error en la linea " + tokens.get(puntero).getNumeroLinea() + ": Se esperaba un operador");
				error = true;
			}
			return false;
		}
		return false;
	}
}