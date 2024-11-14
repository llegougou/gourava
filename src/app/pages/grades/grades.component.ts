import { Component, OnInit } from '@angular/core';
import { ItemService, Item } from '../../service/item.service';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';

interface Criteria {
  name: string;
  rating: number;
}

@Component({
  selector: 'gourava-grades',
  standalone: true,
  imports: [ItemInfoCardComponent, CommonModule, ReactiveFormsModule,FormsModule],
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
  selectedTag: string = '';
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

    if (this.selectedTag) {
      tempItems = tempItems.filter(item =>
        item.tags.some(tag => tag.name === this.selectedTag)
      );
    }

    this.filteredItems = this.sortItems(tempItems);
  }

  sortItems(items: Item[]): Item[] {
    return items.sort((a, b) => {
      if (this.sortBy === 'title') {
        return a.title.localeCompare(b.title);
      } else if (this.sortBy === 'rating') {
        const avgRatingA = this.getAverageRating(a.criterias);
        const avgRatingB = this.getAverageRating(b.criterias);
        return avgRatingB - avgRatingA;  
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

  onSearchChange(): void {
    this.filterItems(); 
  }

  onFilterChange(): void {
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

  closeModal() {
    this.showModal = false;
    this.resetForm();
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

  addTag() {
    if (this.tags.length < this.maxTags) {
      this.tags.push(this.fb.control(''));
    }
  }

  addCriteria() {
    if (this.criterias.length < this.maxCriteria) {
      this.criterias.push(
        this.fb.group({
          name: ['', Validators.required],
          rating: [0, [Validators.required, Validators.min(0), Validators.max(10)]]
        })
      );
    }
  }

  removeTag(index: number) {
    this.tags.removeAt(index);
  }

  removeCriteria(index: number) {
    this.criterias.removeAt(index);
  }

  updateItem(itemId: number): void {
    this.currentItem = this.items.find(item => item.id === itemId) || null;

    if (this.currentItem) {
      this.addItemForm.patchValue({
        title: this.currentItem.title,
      });

      this.tags.clear();
      this.criterias.clear();

      this.currentItem.tags.forEach(tag => this.tags.push(this.fb.control(tag.name)));
      this.currentItem.criterias.forEach(criteria => {
        this.criterias.push(
          this.fb.group({
            name: [criteria.name, Validators.required],
            rating: [criteria.rating, [Validators.required, Validators.min(0), Validators.max(10)]],
          })
        );
      });

      this.showModal = true;
    }
  }

  onSubmit() {
    if (this.addItemForm.valid) {
      const formValues = this.addItemForm.value;

      const formattedTags = formValues.tags.map((tag: string) => ({ name: tag }));

      const formattedCriterias = formValues.criterias.map((criteria: any) => ({
        name: criteria.name,
        rating: criteria.rating,
      }));

      const updatedItem: Item = {
        id: this.currentItem ? this.currentItem.id : 0, 
        title: formValues.title,
        tags: formattedTags,
        criterias: formattedCriterias,
      };

      if (this.currentItem) {
        this.itemService.updateItem(this.currentItem.id, updatedItem).subscribe({
          next: (item) => {
            this.fetchItems(0);
            this.closeModal();
            this.resetForm();
            this.currentItem = null; 
          },
          error: (error) => console.error('Error updating item:', error),
        });
      } else {
        this.itemService.createItem(updatedItem).subscribe({
          next: (item) => {
            this.fetchItems(0);
            this.closeModal();
            this.resetForm();
          },
          error: (error) => console.error('Error adding item:', error),
        });
      }
    }
  }
}
