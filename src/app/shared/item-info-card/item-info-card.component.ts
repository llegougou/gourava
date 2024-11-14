import { Component, EventEmitter, Input, Output, } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { CommonModule } from '@angular/common';

interface Criteria {
  name: string;
  rating: number;
}

interface Tag {
  name: string
}

@Component({
  selector: 'gourava-item-info-card',
  standalone: true,
  imports: [CommonModule, FaIconComponent],
  templateUrl: './item-info-card.component.html',
  styleUrl: './item-info-card.component.scss'
})
export class ItemInfoCardComponent {
  @Input() itemId: number = 0;
  @Input() title: string = '';
  @Input() tags: Tag[] = [];
  @Input() criterias: Criteria[] = [];
  @Input() showButtons: boolean = false;
  @Output() delete = new EventEmitter<number>();
  @Output() update = new EventEmitter<number>();

  renderStars(rating: number): string[] {
    const stars: string[] = [];
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;  
  
    for (let i = 0; i < fullStars; i++) {
      stars.push('full');
    }
  
    if (hasHalfStar) {
      stars.push('half');
    }
  
    while (stars.length < 10) {
      stars.push('empty');
    }
  
    return stars;
  }

  onDelete(): void {
    this.delete.emit(this.itemId);
  }

  onUpdate(): void {
    this.update.emit(this.itemId);
  }
}
