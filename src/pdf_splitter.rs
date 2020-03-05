
use lopdf::Document;
use std::path::{PathBuf, Path};
use std::str::FromStr;

pub fn split_on_qr(_file: PathBuf, output: String) {
    let filename = &_file.file_name().unwrap();
    let mut doc = Document::load(&_file).unwrap();

    let pages = "1";
    let page_numbers = compute_page_numbers(pages);
    let total = *doc.get_pages().keys().max().unwrap_or(&0);
    let page_numbers = complement_page_numbers(&page_numbers, total);
    doc.delete_pages(&page_numbers);
    let mut path = PathBuf::new();
    path.push(output);
    path.push(filename);
    let final_path = Path::new(path.to_str().unwrap());
    doc.save(final_path);
}

fn compute_page_numbers(pages: &str) -> Vec<u32> {
    let mut page_numbers = vec![];
    for page in pages.split(',') {
        let nums: Vec<u32> = page.split('-').map(|num| u32::from_str(num).unwrap()).collect();
        match nums.len() {
            1 => page_numbers.push(nums[0]),
            2 => page_numbers.append(&mut (nums[0]..nums[1] + 1).collect()),
            _ => {}
        }
    }
    page_numbers
}

fn complement_page_numbers(pages: &[u32], total: u32) -> Vec<u32> {
    let mut page_numbers = vec![];
    for page in 1..(total + 1) {
        if !pages.contains(&page) {
            page_numbers.push(page);
        }
    }
    page_numbers
}