import { Component } from '@angular/core';
import { ItemService, Item, Tag, Criteria } from '../../service/item.service';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';
import { CommonModule } from '@angular/common';
import { FormModalComponent } from '../../shared/form-modal/form-modal.component';

interface UsageCount {
  name: string;
  usage_count: number;
}

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
  existingTags: string[] = [];
  existingCriterias: string[] = [];
  tagsUsageCount: UsageCount[] = [];
  criteriasUsageCount: UsageCount[] = [];

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

  ngOnInit(): void {
    this.fetchTagsUsageCount(5);
    this.fetchCriteriasUsageCount(5);
    this.fetchTagsList();
    this.fetchCriteriasList();
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

  fetchCriteriasUsageCount(limit: number): void {
    this.itemService.getCriteriasStatsRandom(limit).subscribe({
      next: (criterias: any[]) => {
        this.criteriasUsageCount = criterias.map(criteria => ({
          name: criteria[0],
          usage_count: criteria[1],
        }));
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  fetchCriteriasList(): void {
    this.itemService.getCriteriasStats(0).subscribe({
      next: (criterias: any[]) => {
        this.existingCriterias = criterias.map(criteria =>
          criteria[0]);
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  fetchTagsUsageCount(limit: number): void {
    this.itemService.getTagsStatsRandom(limit).subscribe({
      next: (tags: any[]) => {
        this.tagsUsageCount = tags.map(tag => ({
          name: tag[0],
          usage_count: tag[1],
        }));
      },
      error: (error) => {
        console.error('Error fetching items:', error);
      }
    })
  }

  fetchTagsList(): void {
    this.itemService.getTagsStats(0).subscribe({
      next: (tags: any[]) => {
        this.existingTags = tags.map(tag => tag[0]);
      },
      error: (error) => {
        console.error('Error fetching tags:', error);
      }
    });
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
