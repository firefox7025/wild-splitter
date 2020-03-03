extern crate glob;
mod file_finder;

fn main() {
    let x = file_finder::find_files("/Users/alexander.montgomery/Desktop");
    println!("{:?}", x);
}


