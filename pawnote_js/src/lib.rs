use wasm_bindgen::prelude::*;

#[wasm_bindgen]
pub fn hello() -> String {
  pawnote::hello()
}
