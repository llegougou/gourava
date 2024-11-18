import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { Item } from '../../service/item.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'gourava-form-modal',
  standalone: true,
  templateUrl: './form-modal.component.html',
  styleUrls: ['./form-modal.component.scss'],
  imports: [ReactiveFormsModule, CommonModule]
})
export class FormModalComponent implements OnInit {
  @Input() showModal: boolean = false;
  @Input() currentItem: Item | null = null;
  @Input() tagsList: string[] = [];
  @Input() criteriasList: string[] = [];
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<Item>();

  itemForm: FormGroup;
  maxTags = 3;
  maxCriteria = 3;

  customTags: boolean[] = [];
  customCriteria: boolean[] = [];

  constructor(private fb: FormBuilder) {
    this.itemForm = this.fb.group({
      title: ['', Validators.required],
      tags: this.fb.array([this.fb.control('')]),
      criterias: this.fb.array([
        this.fb.group({
          name: ['', Validators.required],
          rating: [0, [Validators.required, Validators.min(0), Validators.max(10)]]
        })
      ])
    });
  }

  ngOnInit(): void {
    if (this.currentItem) {
      this.patchForm(this.currentItem);
    }
  }

  ngOnChanges(): void {
    if (this.currentItem) {
      this.patchForm(this.currentItem);
    } else {
      this.itemForm.reset();
      this.tags.clear();
      this.criterias.clear();
      this.addTag();
      this.addCriteria();
    }
  }

  get tags(): FormArray {
    return this.itemForm.get('tags') as FormArray;
  }

  get criterias(): FormArray {
    return this.itemForm.get('criterias') as FormArray;
  }

  patchForm(item: Item): void {
    this.itemForm.patchValue({
      title: item.title,
    });

    this.tags.clear();
    this.criterias.clear();
    this.customTags = [];
    this.customCriteria = [];

    item.tags.forEach(tag => {
      this.tags.push(this.fb.control(tag.name));
      this.customTags.push(false);
    });

    item.criterias.forEach(criteria => {
      this.criterias.push(
        this.fb.group({
          name: [criteria.name, Validators.required],
          rating: [criteria.rating, [Validators.required, Validators.min(0), Validators.max(10)]],
        })
      );
      this.customCriteria.push(false);
    });
  }

  addTag(): void {
    if (this.tags.length < this.maxTags) {
      this.tags.push(this.fb.control(''));
      this.customTags.push(false);
    }
  }

  removeTag(index: number): void {
    this.tags.removeAt(index);
    this.customTags.splice(index, 1);
  }

  addCriteria(): void {
    if (this.criterias.length < this.maxCriteria) {
      this.criterias.push(
        this.fb.group({
          name: ['', Validators.required],
          rating: [0, [Validators.required, Validators.min(0), Validators.max(10)]]
        })
      );
      this.customCriteria.push(false);
    }
  }

  removeCriteria(index: number): void {
    this.criterias.removeAt(index);
    this.customCriteria.splice(index, 1);
  }

  selectCustomTag(index: number): void {
    this.customTags[index] = true;
    this.tags.at(index).setValue('');
  }

  selectCustomCriteria(index: number): void {
    this.customCriteria[index] = true;
    this.criterias.at(index).get('name')?.setValue('');
  }

  closeModal(): void {
    this.close.emit();
  }

  onSubmit(): void {
    if (this.itemForm.valid) {
      const formValues = this.itemForm.value;
      const formattedItem: Item = {
        id: this.currentItem?.id || 0,
        title: formValues.title,
        tags: formValues.tags.map((tag: string) => ({ name: tag })),
        criterias: formValues.criterias,
      };
      this.save.emit(formattedItem);
    }
  }
}
