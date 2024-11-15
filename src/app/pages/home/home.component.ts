import { Component } from '@angular/core';
import { ItemService, Item, Tag, Criteria } from '../../service/item.service';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';
import { CommonModule } from '@angular/common';
import { FormModalComponent } from '../../shared/form-modal/form-modal.component';


@Component({
  selector: 'gourava-home',
  standalone: true,
  imports: [CommonModule, ItemInfoCardComponent, ReactiveFormsModule, FormModalComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent {
  items: Item[] = [];
  addItemForm: FormGroup;
  showModal = false;
  maxTags = 3;
  maxCriteria = 3;
  existingTags: Tag[] = [];
  existingCriterias: Criteria[] = [];

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

    this.fetchItems(2);
  }

  get tags() {
    return this.addItemForm.get('tags') as FormArray;
  }

  get criterias() {
    return this.addItemForm.get('criterias') as FormArray;
  }

  fetchItems(limit: number): void {
    this.itemService.getItems(limit).subscribe({
      next: (data) => {
        this.items = data;
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    });
  }

  fetchExistingCriterias(limit: number): void {
    this.itemService.getCriteriasStats(limit).subscribe({
      next: (data) => {
        this.existingCriterias = data;
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  fetchExistingTags(limit: number): void {
    this.itemService.getTagsStats(limit).subscribe({
      next: (data) => {
        this.existingTags = data;
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  openModal() {
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.resetForm();
  }

  onSaveItem(item: Item): void {
    this.itemService.createItem(item).subscribe(() => this.fetchItems(2));
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

}
