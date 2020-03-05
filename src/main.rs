extern crate glob;
extern crate clap;
extern crate dirs;
extern crate lopdf;
extern crate pyo3;

use clap::{Arg, App};
use std::fs;
use pyo3::prelude::*;
use pyo3::types::IntoPyDict;
use pyo3::Python;

mod file_finder;
mod pdf_splitter;

fn main() {
    let _matches = App::new("Wild Batch Splitter")
                          .version("1.0")
                          .author("Alexander Montgomery <alexandermontgomery95@gmail.com>")
                          .about("Takes a manheim batch and splits them.")
                          .arg(Arg::with_name("INPUT")
                               .help("Sets the input file to use")
                               .required(false)
                               .index(1))
                          .arg(Arg::with_name("OUTPUT")
                               .help("Sets the output folder to use")
                               .required(false)
                               .index(2))
        .get_matches();

    let alternative_home = get_home();
    let alternative_stored = create_output();

    let _input_dir = _matches.value_of("INPUT").unwrap_or(&alternative_home);
    let _out_dir = _matches.value_of("OUTPUT").unwrap_or(&alternative_stored);
    let _located_files = file_finder::find_files(_input_dir);

    for x in _located_files {
        println!("{:?}", &x);
        pdf_splitter::split_on_qr(x, _out_dir.parse().unwrap());
    }

    let gil = Python::acquire_gil();
    let py = gil.python();

    main_(py);
}

fn main_(py: Python) -> PyResult<()> {
    let sys = py.import("sys")?;
    let version: String = sys.get("version")?.extract()?;
    let locals = [("os", py.import("os")?)].into_py_dict(py);
    let code = "os.getenv('USER') or os.getenv('USERNAME') or 'Unknown'";
    let user: String = py.eval(code, None, Some(&locals))?.extract()?;
    println!("Hello {}, I'm Python {}", user, version);
    Ok(())
}

fn get_home() -> String {
    let homedir = dirs::home_dir().unwrap().into_os_string().into_string().unwrap();
    return homedir;
}

fn create_output() -> String {
    let home = dirs::home_dir().unwrap().join("wild").into_os_string().into_string().unwrap();
    fs::create_dir_all(&home);
    return home;
}
