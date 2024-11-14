import { Component } from '@angular/core';
import { ItemService, Item } from '../../service/item.service';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'gourava-home',
  standalone: true,
  imports: [CommonModule, ItemInfoCardComponent, ReactiveFormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  items: Item[] = [];
  addItemForm: FormGroup;
  showModal = false;
  maxTags = 3;
  maxCriteria = 3;

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

  onSubmit() {
    if (this.addItemForm.valid) {
      const formValues = this.addItemForm.value;

      const formattedTags = formValues.tags.map((tag: string) => ({ name: tag }));

      const formattedCriterias = formValues.criterias.map((criteria: any) => ({
        name: criteria.name,
        rating: criteria.rating,
      }));

      const newItem: Item = {
        id: 0,
        title: formValues.title,
        tags: formattedTags,
        criterias: formattedCriterias,
      };

      this.itemService.createItem(newItem).subscribe({
        next: (item) => {
          this.fetchItems(2);
          this.closeModal();
          this.resetForm();
        },
        error: (error) => console.error('Error adding item:', error),
      });
    }
  }
}
