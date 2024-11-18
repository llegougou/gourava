import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../service/item.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

interface UsageCount {
  name: string;
  usage_count: number;
}

@Component({
  selector: 'gourava-stats',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.scss']
})

export class StatsComponent implements OnInit {
  allTags: UsageCount[] = [];
  allCriterias: UsageCount[] = [];

  constructor(private itemService: ItemService) {}

  ngOnInit(): void {
    this.fetchAllTags();
    this.fetchAllCriterias();
  }

  fetchAllTags(): void {
    this.itemService.getTagsStats().subscribe({
      next: (tags: any[]) => {
        this.allTags = tags.map(tag => ({
          name: tag[0],
          usage_count: tag[1],
        }));
      },
      error: (err) => {
        console.error('Error fetching tags:', err);
      },
    });
  }

  fetchAllCriterias(): void {
    this.itemService.getCriteriasStats().subscribe({
      next: (criterias: any[]) => {
        this.allCriterias = criterias.map(criteria => ({
          name: criteria[0],
          usage_count: criteria[1],
        }));
      },
      error: (err) => {
        console.error('Error fetching criterias:', err);
      },
    });
  }
}
