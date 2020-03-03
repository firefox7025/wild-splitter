use glob::glob;
use std::path::PathBuf;
use std::path::Path;


pub fn find_files(root_path: &str) -> Vec<PathBuf> {

    let glob_path = Path::new(root_path).join("**").join("*.pdf");
    let mut paths = Vec::new();

    let glob_string = glob_path.to_str().unwrap();

    for entry in glob(glob_string).expect("Failed to read glob pattern") {
        match entry {
            Ok(path) => paths.push(path),
            Err(e) => println!("{:?}", e)
        }
    }
    return paths;
}
