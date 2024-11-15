import { Component, OnInit } from '@angular/core';
import { ItemService, Item } from '../../service/item.service';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormModalComponent } from '../../shared/form-modal/form-modal.component';

interface Criteria {
  name: string;
  rating: number;
}

@Component({
  selector: 'gourava-grades',
  standalone: true,
  imports: [ItemInfoCardComponent, CommonModule, ReactiveFormsModule, FormsModule, FormModalComponent],
  templateUrl: './grades.component.html',
  styleUrls: ['./grades.component.scss'],
})

export class GradesComponent {
  items: Item[] = [];
  filteredItems: Item[] = []; 
  searchQuery: string = '';
  addItemForm: FormGroup;
  showModal = false;
  maxTags = 3;
  maxCriteria = 3;
  currentItem: Item | null = null;
  selectedTags: string[] = [];
  sortBy: string = 'title';
  allTags: string[] = [];

  constructor(private itemService: ItemService, private fb: FormBuilder) {
    this.addItemForm = this.fb.group({
      title: ['', Validators.required],
      tags: this.fb.array([this.fb.control('')]),
      criterias: this.fb.array([
        this.fb.group({
          name: ['', Validators.required],
          rating: [0, [Validators.required, Validators.min(0), Validators.max(10)]]
        })
      ])
    });

    this.fetchItems(0); 
  }

  fetchItems(limit: number): void {
    this.itemService.getItems(limit).subscribe({
      next: (data) => {
        this.items = data;
        this.allTags = this.getAllTags();
        this.filterItems();
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    });
  }

  getAllTags(): string[] {
    const tags = this.items.flatMap(item => item.tags.map(tag => tag.name));
    return Array.from(new Set(tags));
  }

  filterItems(): void {
    let tempItems = [...this.items];
  
    if (this.searchQuery) {
      tempItems = tempItems.filter(item =>
        this.matchSearchQuery(item, this.searchQuery)
      );
    }
  
    if (this.selectedTags.length > 0) {
      tempItems = tempItems.filter(item =>
        this.selectedTags.some(tag => item.tags.some(itemTag => itemTag.name === tag))
      );
    }
  
    this.filteredItems = this.sortItems(tempItems);
  }

  sortItems(items: Item[]): Item[] {
    return items.sort((a, b) => {
      if (this.sortBy === 'title') {
        return a.title.localeCompare(b.title);
      } else if (this.sortBy === 'desc') {
        const avgRatingA = this.getAverageRating(a.criterias);
        const avgRatingB = this.getAverageRating(b.criterias);
        return avgRatingB - avgRatingA;  
      }else if (this.sortBy === 'asc') {
        const avgRatingA = this.getAverageRating(a.criterias);
        const avgRatingB = this.getAverageRating(b.criterias);
        return avgRatingA - avgRatingB;  
      }
      return 0;
    });
  }

  getAverageRating(criterias:Criteria[]): number {
    const totalRating = criterias.reduce((sum, criteria) => sum + criteria.rating, 0);
    return criterias.length ? totalRating / criterias.length : 0;
  }

  matchSearchQuery(item: Item, query: string): boolean {
    const lowerCaseQuery = query.toLowerCase();

    return (
      item.title.toLowerCase().includes(lowerCaseQuery) ||
      item.tags.some(tag => tag.name.toLowerCase().includes(lowerCaseQuery)) ||
      item.criterias.some(criteria => criteria.name.toLowerCase().includes(lowerCaseQuery))
    );
  }

  toggleTagSelection(tag: string): void {
    if (this.selectedTags.includes(tag)) {
      this.selectedTags = this.selectedTags.filter(selectedTag => selectedTag !== tag);
    } else {
      this.selectedTags.push(tag);
    }
    this.filterItems();
  }

  onSearchChange(): void {
    this.filterItems(); 
  }

  onSortChange(): void {
    this.filteredItems = this.sortItems(this.filteredItems);  
  }

  deleteItem(id: number): void {
    this.itemService.deleteItem(id).subscribe({
      next: () => {
        this.items = this.items.filter(item => item.id !== id);
        this.filterItems(); 
        console.log(`Item with id ${id} deleted successfully.`);
      },
      error: (error) => {
        console.error('Error deleting item:', error);
      }
    });
  }

  get tags() {
    return this.addItemForm.get('tags') as FormArray;
  }

  get criterias() {
    return this.addItemForm.get('criterias') as FormArray;
  }

  openModal() {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentItem = null;
    this.resetForm();
  }

  onSaveItem(item: Item): void {
    if (item.id) {
      this.itemService.updateItem(item.id, item).subscribe(() => this.fetchItems(0));
    } 
    this.closeModal();
  }

  resetForm() {
    this.addItemForm.reset();
    this.tags.clear();
    this.criterias.clear();
    this.tags.push(this.fb.control(''));
    this.criterias.push(this.fb.group({
      name: ['', Validators.required],
      rating: [0, [Validators.required, Validators.min(0), Validators.max(10)]]
    }));
  }
  
  updateItem(itemId: number): void {
    const itemToEdit = this.items.find(item => item.id === itemId);
  
    if (itemToEdit) {
      this.currentItem = itemToEdit;
      this.showModal = true;
    }
  }
}
